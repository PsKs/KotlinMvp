package com.hazz.kotlinmvp.mvp.presenter

import android.app.Activity
import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.dataFormat
import com.hazz.kotlinmvp.mvp.contract.VideoDetailContract
import com.hazz.kotlinmvp.mvp.model.VideoDetailModel
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.net.exception.ExceptionHandle
import com.hazz.kotlinmvp.showToast
import com.hazz.kotlinmvp.utils.DisplayManager
import com.hazz.kotlinmvp.utils.NetworkUtil

/**
 * Created by xuhao on 2017/11/25.
 * desc:
 */
class VideoDetailPresenter : BasePresenter<VideoDetailContract.View>(), VideoDetailContract.Presenter {

    private val videoDetailModel: VideoDetailModel by lazy {

        VideoDetailModel()
    }

    /**
     * Load video-related data
     */
    override fun loadVideoInfo(itemInfo: HomeBean.Issue.Item) {

        val playInfo = itemInfo.data?.playInfo

        val netType = NetworkUtil.isWifi(MyApplication.context)

        // Check whether View is bound
        checkViewAttached()

        if (playInfo!!.size > 1) {
            // The current network is a high-definition video in the Wifi environment
            if (netType) {
                for (i in playInfo) {
                    if (i.type == "high") {
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        break
                    }
                }
            } else {
                // Otherwise, choose SD video
                for (i in playInfo) {
                    if (i.type == "normal") {
                        val playUrl = i.url
                        mRootView?.setVideo(playUrl)
                        // Todo To be perfected
                        (mRootView as Activity).showToast("Consumption${(mRootView as Activity)
                                .dataFormat(i.urlList[0].size)}flow")
                        break
                    }
                }
            }
        } else {
            mRootView?.setVideo(itemInfo.data.playUrl)
        }

        // Set background
        val backgroundUrl = itemInfo.data.cover.blurred + "/thumbnail/${DisplayManager.getScreenHeight()!! - DisplayManager.dip2px(250f)!!}x${DisplayManager.getScreenWidth()}"
        backgroundUrl.let { mRootView?.setBackground(it) }

        mRootView?.setVideoInfo(itemInfo)
    }


    /**
     * Request related video data
     */
    override fun requestRelatedVideo(id: Long) {
        mRootView?.showLoading()
        val disposable = videoDetailModel.requestRelatedData(id)
                .subscribe({ issue ->
                    mRootView?.apply {
                        dismissLoading()
                        setRecentRelatedVideo(issue.itemList)
                    }
                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        setErrorMsg(ExceptionHandle.handleException(t))
                    }
                })

        addSubscription(disposable)
    }
}