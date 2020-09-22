package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: Follow Contract
 */
interface FollowContract {

    interface View : IBaseView {
        /**
         * Set attention information data
         */
        fun setFollowInfo(issue: HomeBean.Issue)

        fun showError(errorMsg: String, errorCode: Int)
    }


    interface Presenter : IPresenter<View> {
        /**
         * Get List
         */
        fun requestFollowList()

        /**
         * load more
         */
        fun loadMoreData()
    }
}