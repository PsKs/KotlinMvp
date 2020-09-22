package com.hazz.kotlinmvp.mvp.contract

import com.hazz.kotlinmvp.base.IBaseView
import com.hazz.kotlinmvp.base.IPresenter
import com.hazz.kotlinmvp.mvp.model.bean.TabInfoBean

/**
 * Created by xuhao on 2017/11/30.
 * desc: Tab Contract
 */
interface HotTabContract {

    interface View:IBaseView{
        /**
         * Set up TabInfo
         */
        fun setTabInfo(tabInfoBean: TabInfoBean)

        fun showError(errorMsg:String,errorCode:Int)
    }

    interface Presenter:IPresenter<View>{
        /**
         * Get TabInfo
         */
        fun getTabInfo()
    }
}