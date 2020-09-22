package com.hazz.kotlinmvp.view.recyclerview.adapter

/**
 *
 * Description: Long press event of Adapter item
 */
interface OnItemLongClickListener {
    fun onItemLongClick(obj: Any?, position: Int): Boolean
}
