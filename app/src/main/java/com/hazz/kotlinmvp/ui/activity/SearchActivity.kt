package com.hazz.kotlinmvp.ui.activity

import android.annotation.TargetApi
import android.graphics.Typeface
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.transition.Fade
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.KeyEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.google.android.flexbox.*
import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.mvp.contract.SearchContract
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.presenter.SearchPresenter
import com.hazz.kotlinmvp.net.exception.ErrorStatus
import com.hazz.kotlinmvp.showToast
import com.hazz.kotlinmvp.ui.adapter.CategoryDetailAdapter
import com.hazz.kotlinmvp.ui.adapter.HotKeywordsAdapter
import com.hazz.kotlinmvp.utils.CleanLeakUtils
import com.hazz.kotlinmvp.utils.StatusBarUtil
import com.hazz.kotlinmvp.view.ViewAnimUtils
import kotlinx.android.synthetic.main.activity_search.*

/**
 * Created by xuhao on 2017/12/1.
 * desc:Search function
 */

class SearchActivity : BaseActivity(), SearchContract.View {

    private val mPresenter by lazy { SearchPresenter() }

    private val mResultAdapter by lazy { CategoryDetailAdapter(this, itemList, R.layout.item_category_detail) }

    private var mHotKeywordsAdapter: HotKeywordsAdapter? = null

    private var itemList = ArrayList<HomeBean.Issue.Item>()

    private var mTextTypeface: Typeface? = null

    private var keyWords: String? = null

    /**
     * Whether to load more
     */
    private var loadingMore = false

    init {
        mPresenter.attachView(this)
        // Thin black simplified font
        mTextTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    override fun layoutId(): Int = R.layout.activity_search

    /**
     * Enter the page
     */
    override fun initData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setUpEnterAnimation() // Start animation
            setUpExitAnimation() // Exit animation
        } else {
            setUpView()
        }
    }

    override fun initView() {
        tv_title_tip.typeface = mTextTypeface
        tv_hot_search_words.typeface = mTextTypeface
        // Initialize the RecyclerView of the query result
        mRecyclerView_result.layoutManager = LinearLayoutManager(this)
        mRecyclerView_result.adapter = mResultAdapter

        // Realize automatic loading
        mRecyclerView_result.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView_result.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView_result.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        // cancel
        tv_cancel.setOnClickListener { onBackPressed() }
        // Keyboard search button
        et_search_view.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    closeSoftKeyboard()
                    keyWords = et_search_view.text.toString().trim()
                    if (keyWords.isNullOrEmpty()) {
                        showToast("Please enter the keywords you are interested in")
                    } else {
                        mPresenter.querySearchData(keyWords!!)
                    }
                }
                return false
            }

        })

        mLayoutStatusView = multipleStatusView

        // Status bar transparency and spacing processing
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

    }
    /**
     * Close the software disk
     */
    override fun closeSoftKeyboard() {
        closeKeyBord(et_search_view, applicationContext)
    }

    override fun start() {
        // Request popular keywords
        mPresenter.requestHotWordData()
    }

    override fun showLoading() {
        mLayoutStatusView?.showLoading()
    }

    override fun dismissLoading() {
        mLayoutStatusView?.showContent()
    }

    /**
     * Set popular keywords
     */
    override fun setHotWordData(string: ArrayList<String>) {
        showHotWordView()
        mHotKeywordsAdapter = HotKeywordsAdapter(this, string, R.layout.item_flow_text)

        val flexBoxLayoutManager = FlexboxLayoutManager(this)
        flexBoxLayoutManager.flexWrap = FlexWrap.WRAP      // Wrap in the normal direction
        flexBoxLayoutManager.flexDirection = FlexDirection.ROW   // The main axis is horizontal, and the starting point is at the left end
        flexBoxLayoutManager.alignItems = AlignItems.CENTER    // Define how items are aligned on the counter axis
        flexBoxLayoutManager.justifyContent = JustifyContent.FLEX_START  // Multiple axis alignment

        mRecyclerView_hot.layoutManager = flexBoxLayoutManager
        mRecyclerView_hot.adapter = mHotKeywordsAdapter
        // Set tag click event
        mHotKeywordsAdapter?.setOnTagItemClickListener {
            closeSoftKeyboard()
            keyWords = it
            mPresenter.querySearchData(it)
        }
    }

    /**
     * Set search results
     */
    override fun setSearchResult(issue: HomeBean.Issue) {
        loadingMore = false

        hideHotWordView()
        tv_search_count.visibility = View.VISIBLE
        tv_search_count.text = String.format(resources.getString(R.string.search_result_count), keyWords, issue.total)

        itemList = issue.itemList
        mResultAdapter.addData(issue.itemList)
    }

    override fun showError(errorMsg: String, errorCode: Int) {
        showToast(errorMsg)
        if (errorCode == ErrorStatus.NETWORK_ERROR) {
            mLayoutStatusView?.showNoNetwork()
        } else {
            mLayoutStatusView?.showError()
        }
    }

    /**
     * No match found
     */
    override fun setEmptyView() {
        showToast("Sorry, no matches were found")
        hideHotWordView()
        tv_search_count.visibility = View.GONE
        mLayoutStatusView?.showEmpty()
    }

    /**
     * Hide View of Popular Keywords
     */
    private fun hideHotWordView(){
        layout_hot_words.visibility = View.GONE
        layout_content_result.visibility = View.VISIBLE
    }

    /**
     * Streaming layout showing popular keywords
     */
    private fun showHotWordView(){
        layout_hot_words.visibility = View.VISIBLE
        layout_content_result.visibility = View.GONE
    }

    /**
     * Exit animation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpExitAnimation() {
        val fade = Fade()
        window.returnTransition = fade
        fade.duration = 300
    }

    /**
     * Enter Animation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setUpEnterAnimation() {
        val transition = TransitionInflater.from(this)
                .inflateTransition(R.transition.arc_motion)
        window.sharedElementEnterTransition = transition
        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        })
    }

    private fun setUpView() {
        val animation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in)
        animation.duration = 300
        rel_container.startAnimation(animation)
        rel_container.visibility = View.VISIBLE
        // Open soft keyboard
        openKeyBord(et_search_view, applicationContext)
    }


    /**
     * Show animation
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun animateRevealShow() {
        ViewAnimUtils.animateRevealShow(
                this, rel_frame,
                fab_circle.width / 2, R.color.backgroundColor,
                object : ViewAnimUtils.OnRevealAnimationListener {
                    override fun onRevealHide() {

                    }

                    override fun onRevealShow() {
                        setUpView()
                    }
                })
    }


    // Return event
    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewAnimUtils.animateRevealHide(
                    this, rel_frame,
                    fab_circle.width / 2, R.color.backgroundColor,
                    object : ViewAnimUtils.OnRevealAnimationListener {
                        override fun onRevealHide() {
                            defaultBackPressed()
                        }

                        override fun onRevealShow() {

                        }
                    })
        } else {
            defaultBackPressed()
        }
    }

    // Default fallback
    private fun defaultBackPressed() {
        closeSoftKeyboard()
        super.onBackPressed()
    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        mPresenter.detachView()
        mTextTypeface = null
    }


}
