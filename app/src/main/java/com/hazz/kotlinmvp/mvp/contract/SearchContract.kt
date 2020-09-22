package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: Search contract
 */
interface SearchContract {

    interface View : IBaseView {
        /**
         * Set up popular keyword data
         */
        fun setHotWordData(string: ArrayList<String>)

        /**
         * Set the results returned by search keywords
         */
        fun setSearchResult(issue:HomeBean.Issue)
        /**
         * Close the software Keyboard
         */
        fun closeSoftKeyboard()

        /**
         * Set empty View
         */
        fun setEmptyView()


        fun showError(errorMsg: String,errorCode:Int)
    }


    interface Presenter : IPresenter<View> {
        /**
         * Get data on popular keywords
         */
        fun requestHotWordData()

        /**
         * Query search
         */
        fun querySearchData(words:String)

        /**
         * load more
         */
        fun loadMoreData()
    }
}