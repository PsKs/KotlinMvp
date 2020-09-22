package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/25.
 * desc: Video details contract
 */
interface VideoDetailContract {

    interface View : IBaseView {

        /**
         * Video details contract
         */
        fun setVideo(url: String)

        /**
         * Set video information
         */
        fun setVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * Set background
         */
        fun setBackground(url: String)

        /**
         * Set up the latest related videos
         */
        fun setRecentRelatedVideo(itemList: ArrayList<HomeBean.Issue.Item>)

        /**
         * Set error message
         */
        fun setErrorMsg(errorMsg: String)
    }

    interface Presenter : IPresenter<View> {

        /**
         * Load video information
         */
        fun loadVideoInfo(itemInfo: HomeBean.Issue.Item)

        /**
         * Request related video data
         */
        fun requestRelatedVideo(id: Long)
    }
}