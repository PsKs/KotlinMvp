package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean

/**
 * Created by xuhao on 2017/11/29.
 * desc: Classification Contract
 */
interface CategoryContract {

    interface View : IBaseView {
        /**
         * Display classified information
         */
        fun showCategory(categoryList: ArrayList<CategoryBean>)

        /**
         * Show error message
         */
        fun showError(errorMsg:String,errorCode:Int)
    }

    interface Presenter:IPresenter<View>{
        /**
         * Get classified information
         */
        fun getCategoryData()
    }
}