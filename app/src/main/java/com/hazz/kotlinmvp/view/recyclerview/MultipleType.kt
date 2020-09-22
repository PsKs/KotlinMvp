package com.hazz.kotlinmvp.view.recyclerview

/**
 * Created by xuhao on 2017/11/22.
 * desc: Multiple layout item types
 */
interface MultipleType<in T> {
    fun getLayoutId(item: T, position: Int): Int
}
