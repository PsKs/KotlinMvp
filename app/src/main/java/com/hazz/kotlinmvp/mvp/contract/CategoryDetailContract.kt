package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: Classification Details Contract Class
 */
interface CategoryDetailContract {

    interface View:IBaseView{
        /**
         *  Set list data
         */
        fun setCateDetailList(itemList:ArrayList<HomeBean.Issue.Item>)

        fun showError(errorMsg:String)
    }

    interface Presenter:IPresenter<View>{

        fun getCategoryDetailList(id:Long)

        fun loadMoreData()
    }
}