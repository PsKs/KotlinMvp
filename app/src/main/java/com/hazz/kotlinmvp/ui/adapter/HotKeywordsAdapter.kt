package com.hazz.kotlinmvp.ui.adapter

import android.content.Context
import android.view.View
import android.widget.TextView
import com.google.android.flexbox.FlexboxLayoutManager
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.view.recyclerview.ViewHolder
import com.hazz.kotlinmvp.view.recyclerview.adapter.CommonAdapter

/**
 * Created by xuhao on 2017/12/4.
 * desc: Adapter for Tag label layout
 */
class HotKeywordsAdapter(mContext: Context,mList: ArrayList<String>, layoutId: Int) :
        CommonAdapter<String>(mContext, mList, layoutId) {

    /**
     * Kotlin functions can be used as parameters. When writing callbacks, you don’t need interface
     */
    private var mOnTagItemClick: ((tag:String) -> Unit)? = null

    fun setOnTagItemClickListener(onTagItemClickListener:(tag:String) -> Unit) {
        this.mOnTagItemClick = onTagItemClickListener
    }

    override fun bindData(holder: ViewHolder, data: String, position: Int) {

        holder.setText(R.id.tv_title,data)

        val params = holder.getView<TextView>(R.id.tv_title).layoutParams

        if(params is FlexboxLayoutManager.LayoutParams) {
            params.flexGrow = 1.0f
        }

        holder.setOnItemClickListener( object :View.OnClickListener {
            override fun onClick(v: View?) {
                mOnTagItemClick?.invoke(data)
            }
        })
    }
}