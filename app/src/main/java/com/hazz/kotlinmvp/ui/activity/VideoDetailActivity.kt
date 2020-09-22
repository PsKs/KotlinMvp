package com.hazz.kotlinmvp.ui.activity

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.res.Configuration
import android.os.Build
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.transition.Transition
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.hazz.kotlinmvp.Constants
import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.glide.GlideApp
import com.hazz.kotlinmvp.mvp.contract.VideoDetailContract
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.presenter.VideoDetailPresenter
import com.hazz.kotlinmvp.showToast
import com.hazz.kotlinmvp.ui.adapter.VideoDetailAdapter
import com.hazz.kotlinmvp.utils.CleanLeakUtils
import com.hazz.kotlinmvp.utils.StatusBarUtil
import com.hazz.kotlinmvp.utils.WatchHistoryUtils
import com.hazz.kotlinmvp.view.VideoListener
import com.orhanobut.logger.Logger
import com.scwang.smartrefresh.header.MaterialHeader
import com.shuyu.gsyvideoplayer.listener.LockClickListener
import com.shuyu.gsyvideoplayer.utils.OrientationUtils
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer
import com.shuyu.gsyvideoplayer.video.base.GSYVideoPlayer
import kotlinx.android.synthetic.main.activity_video_detail.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
/**
 * Created by xuhao on 2017/11/25.
 * desc: Video details
 */
class VideoDetailActivity : BaseActivity(), VideoDetailContract.View {

    companion object {
        const val IMG_TRANSITION = "IMG_TRANSITION"
        const val TRANSITION = "TRANSITION"
    }

    /**
     * Initialize on the first call
     */
    private val mPresenter by lazy { VideoDetailPresenter() }

    private val mAdapter by lazy { VideoDetailAdapter(this, itemList) }

    private val mFormat by lazy { SimpleDateFormat("yyyyMMddHHmmss"); }

    /**
     * Item detailed data
     */
    private lateinit var itemData: HomeBean.Issue.Item
    private var orientationUtils: OrientationUtils? = null

    private var itemList = ArrayList<HomeBean.Issue.Item>()

    private var isPlay: Boolean = false
    private var isPause: Boolean = false

    private var isTransition: Boolean = false

    private var transition: Transition? = null
    private var mMaterialHeader: MaterialHeader? = null

    override fun layoutId(): Int = R.layout.activity_video_detail

    /**
     * Initialize View
     */
    override fun initView() {

        mPresenter.attachView(this)
        // Transition animation
        initTransition()
        initVideoViewConfig()

        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        // Set the click event of the related video item
        mAdapter.setOnItemDetailClick { mPresenter.loadVideoInfo(it) }

        // Status bar transparency and spacing processing
        StatusBarUtil.immersive(this)
        StatusBarUtil.setPaddingSmart(this, mVideoView)

        /***  Pull down to refresh  ***/
        // Content follows offset
        mRefreshLayout.setEnableHeaderTranslationContent(true)
        mRefreshLayout.setOnRefreshListener {
            loadVideoInfo()
        }
        mMaterialHeader = mRefreshLayout.refreshHeader as MaterialHeader?
        // Open the drop-down refresh area block background:
        mMaterialHeader?.setShowBezierWave(true)
        // Set drop-down refresh theme color
        mRefreshLayout.setPrimaryColorsId(R.color.color_light_black, R.color.color_title_bg)
    }

    /**
     * Initialize the configuration of VideoView
     */
    private fun initVideoViewConfig() {
        // Set rotation
        orientationUtils = OrientationUtils(this, mVideoView)
        // Whether to rotate
        mVideoView.isRotateViewAuto = false
        // Can slide adjustment
        mVideoView.setIsTouchWiget(true)

        // Add cover
        val imageView = ImageView(this)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        GlideApp.with(this)
                .load(itemData.data?.cover?.feed)
                .centerCrop()
                .into(imageView)
        mVideoView.thumbImageView = imageView

        mVideoView.setStandardVideoAllCallBack(object : VideoListener {

            override fun onPrepared(url: String, vararg objects: Any) {
                super.onPrepared(url, *objects)
                // Start playing to rotate and full screen
                orientationUtils?.isEnable = true
                isPlay = true
            }

            override fun onAutoComplete(url: String, vararg objects: Any) {
                super.onAutoComplete(url, *objects)
                Logger.d("***** onAutoPlayComplete **** ")
            }

            override fun onPlayError(url: String, vararg objects: Any) {
                super.onPlayError(url, *objects)
                showToast("Playback failed")
            }

            override fun onEnterFullscreen(url: String, vararg objects: Any) {
                super.onEnterFullscreen(url, *objects)
                Logger.d("***** onEnterFullscreen **** ")
            }

            override fun onQuitFullscreen(url: String, vararg objects: Any) {
                super.onQuitFullscreen(url, *objects)
                Logger.d("***** onQuitFullscreen **** ")
                // List returned style judgment
                orientationUtils?.backToProtVideo()
            }
        })
        // Set the return button function
        mVideoView.backButton.setOnClickListener({ onBackPressed() })
        // Set the full screen key function
        mVideoView.fullscreenButton.setOnClickListener {
            // Direct horizontal screen
            orientationUtils?.resolveByClick()
            // Does the first true need to hide the actionbar, and if the second true needs to hide the status bar
            mVideoView.startWindowFullscreen(this, true, true)
        }
        // Lock screen event
        mVideoView.setLockClickListener(object : LockClickListener {
            override fun onClick(view: View?, lock: Boolean) {
                // Cooperate with onConfigurationChanged below
                orientationUtils?.isEnable = !lock
            }
        })
    }

    override fun start() {

    }

    /**
     * Initialization data
     */
    override fun initData() {
        itemData = intent.getSerializableExtra(Constants.BUNDLE_VIDEO_DATA) as HomeBean.Issue.Item
        isTransition = intent.getBooleanExtra(TRANSITION, false)

        saveWatchVideoHistoryInfo(itemData)
    }

    /**
     * Save watch history
     */
    private fun saveWatchVideoHistoryInfo(watchItem: HomeBean.Issue.Item) {
        // Before saving, you must first check whether there is a record of the value in sp
        // and delete it. This ensures that there will be no duplicate entries in the search history
        val historyMap = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context) as Map<*, *>
        for ((key, _) in historyMap) {
            if (watchItem == WatchHistoryUtils.getObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context, key as String)) {
                WatchHistoryUtils.remove(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context, key)
            }
        }
        WatchHistoryUtils.putObject(Constants.FILE_WATCH_HISTORY_NAME,MyApplication.context, watchItem,"" + mFormat.format(Date()))
    }


    override fun showLoading() {

    }

    override fun dismissLoading() {
        mRefreshLayout.finishRefresh()
    }

    /**
     * Set playing video URL
     */
    override fun setVideo(url: String) {
        Logger.d("playUrl:$url")
        mVideoView.setUp(url, false, "")
        // Start auto play
        mVideoView.startPlayLogic()
    }

    /**
     * Set video information
     */
    override fun setVideoInfo(itemInfo: HomeBean.Issue.Item) {
        itemData = itemInfo
        mAdapter.addData(itemInfo)
        // Request related latest videos
        mPresenter.requestRelatedVideo(itemInfo.data?.id?:0)

    }

    /**
     * Set related data video
     */
    override fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>) {
        mAdapter.addData(itemList)
        this.itemList = itemList
    }

    /**
     * Set background color
     */
    override fun setBackground(url: String) {
        GlideApp.with(this)
                .load(url)
                .centerCrop()
                .format(DecodeFormat.PREFER_ARGB_8888)
                .transition(DrawableTransitionOptions().crossFade())
                .into(mVideoBackground)
    }

    /**
     * Set error message
     */
    override fun setErrorMsg(errorMsg: String) {

    }


    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        if (isPlay && !isPause) {
            mVideoView.onConfigurationChanged(this, newConfig, orientationUtils)
        }
    }

    /**
     * 1.Load video information
     */
    fun loadVideoInfo() {
        mPresenter.loadVideoInfo(itemData)
    }

    /**
     * Monitor return key
     */
    override fun onBackPressed() {
        orientationUtils?.backToProtVideo()
        if (StandardGSYVideoPlayer.backFromWindowFull(this))
            return
        // Release all
        mVideoView.setStandardVideoAllCallBack(null)
        GSYVideoPlayer.releaseAllVideos()
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) run {
            super.onBackPressed()
        } else {
            finish()
            overridePendingTransition(R.anim.anim_out, R.anim.anim_in)
        }
    }

    override fun onResume() {
        super.onResume()
        getCurPlay().onVideoResume()
        isPause = false
    }

    override fun onPause() {
        super.onPause()
        getCurPlay().onVideoPause()
        isPause = true
    }

    override fun onDestroy() {
        CleanLeakUtils.fixInputMethodManagerLeak(this)
        super.onDestroy()
        GSYVideoPlayer.releaseAllVideos()
        orientationUtils?.releaseListener()
        mPresenter.detachView()
    }

    private fun getCurPlay(): GSYVideoPlayer {
        return if (mVideoView.fullWindowPlayer != null) {
            mVideoView.fullWindowPlayer
        } else mVideoView
    }

    private fun initTransition() {
        if (isTransition && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            postponeEnterTransition()
            ViewCompat.setTransitionName(mVideoView, IMG_TRANSITION)
            addTransitionListener()
            startPostponedEnterTransition()
        } else {
            loadVideoInfo()
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun addTransitionListener() {
        transition = window.sharedElementEnterTransition
        transition?.addListener(object : Transition.TransitionListener {
            override fun onTransitionResume(p0: Transition?) {
            }

            override fun onTransitionPause(p0: Transition?) {
            }

            override fun onTransitionCancel(p0: Transition?) {
            }

            override fun onTransitionStart(p0: Transition?) {
            }

            override fun onTransitionEnd(p0: Transition?) {
                Logger.d("onTransitionEnd()------")

                loadVideoInfo()
                transition?.removeListener(this)
            }

        })
    }
}