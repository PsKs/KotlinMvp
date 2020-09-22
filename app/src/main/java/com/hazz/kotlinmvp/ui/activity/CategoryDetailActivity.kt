package com.hazz.kotlinmvp.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.hazz.kotlinmvp.Constants
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.glide.GlideApp
import com.hazz.kotlinmvp.mvp.contract.CategoryDetailContract
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.presenter.CategoryDetailPresenter
import com.hazz.kotlinmvp.ui.adapter.CategoryDetailAdapter
import com.hazz.kotlinmvp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_category_detail.*

/**
 * Created by xuhao on 2017/11/30.
 * desc: Category details
 */
class CategoryDetailActivity : BaseActivity(), CategoryDetailContract.View {

    private val mPresenter by lazy { CategoryDetailPresenter() }

    private val mAdapter by lazy { CategoryDetailAdapter(this, itemList, R.layout.item_category_detail) }

    private var categoryData: CategoryBean? = null

    private var itemList = ArrayList<HomeBean.Issue.Item>()

    init {
        mPresenter.attachView(this)
    }

    /**
     * Whether to load more
     */
    private var loadingMore = false

    override fun initData() {
        categoryData = intent.getSerializableExtra(Constants.BUNDLE_CATEGORY_DATA) as CategoryBean?
    }

    override fun layoutId(): Int = R.layout.activity_category_detail

    @SuppressLint("SetTextI18n")
    override fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // Load header Image
        GlideApp.with(this)
                .load(categoryData?.headerImage)
                .placeholder(R.color.color_darker_gray)
                .into(imageView)

        tv_category_desc.text ="#${categoryData?.description}#"

        collapsing_toolbar_layout.title = categoryData?.name
        collapsing_toolbar_layout.setExpandedTitleColor(Color.WHITE) // Set the font color when the state is not contracted
        collapsing_toolbar_layout.setCollapsedTitleTextColor(Color.BLACK) // Set the color of the font on the Toolbar after shrinking

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter
        // Realize automatic loading
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val itemCount = mRecyclerView.layoutManager.itemCount
                val lastVisibleItem = (mRecyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (!loadingMore && lastVisibleItem == (itemCount - 1)) {
                    loadingMore = true
                    mPresenter.loadMoreData()
                }
            }
        })

        // Status bar transparency and spacing processing
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

    }

    override fun start() {
        // Get the current category list
        categoryData?.id?.let { mPresenter.getCategoryDetailList(it) }
    }

    override fun showLoading() {

    }

    override fun dismissLoading() {

    }

    override fun setCateDetailList(itemList: ArrayList<HomeBean.Issue.Item>) {
        loadingMore = false
        mAdapter.addData(itemList)
    }

    override fun showError(errorMsg: String) {
        multipleStatusView.showError()
    }


    override fun onDestroy() {
        super.onDestroy()
        mPresenter.detachView()
    }
}