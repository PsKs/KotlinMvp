package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/8.
 * Home Contract
 */
interface HomeContract {

    interface View : IBaseView {

        /**
         * Set the data requested for the first time
         */
        fun setHomeData(homeBean: HomeBean)

        /**
         * Set to load more data
         */
        fun setMoreData(itemList:ArrayList<HomeBean.Issue.Item>)

        /**
         * Show error message
         */
        fun showError(msg: String,errorCode:Int)
    }

    interface Presenter {

        /**
         * Get featured data on the homepage
         */
        fun requestHomeData(num: Int)

        /**
         * Load more data
         */
        fun loadMoreData()
    }
}
