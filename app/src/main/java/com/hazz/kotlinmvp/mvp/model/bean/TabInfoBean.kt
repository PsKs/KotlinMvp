package com.hazz.kotlinmvp.mvp.model.bean

/**
 * Created by xuhao on 2017/11/30.
 * desc: Popular tabInfo
 */

data class TabInfoBean(val tabInfo: TabInfo) {
    data class TabInfo(val tabList: ArrayList<Tab>)

    data class Tab(val id: Long, val name: String, val apiUrl: String)
}
