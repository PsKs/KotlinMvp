package com.hazz.kotlinmvp.view.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import com.hazz.kotlinmvp.view.recyclerview.ViewHolder

/**
 * Created by xuhao on 2017/11/22.
 * desc: (Common) Universal Adapter
 */

abstract class CommonAdapter<T>(var mContext: Context, var mData: ArrayList<T>, // Entry layout
                                private var mLayoutId: Int) : RecyclerView.Adapter<ViewHolder>() {
    protected var mInflater: LayoutInflater? = null
    private var mTypeSupport: MultipleType<T>? = null

    // Use interface to call back click events
    private var mItemClickListener: OnItemClickListener? = null

    // Use interface to call back click events
    private var mItemLongClickListener: OnItemLongClickListener? = null

    init {
        mInflater = LayoutInflater.from(mContext)
    }

    // Need more layout
    constructor(context: Context, data: ArrayList<T>, typeSupport: MultipleType<T>) : this(context, data, -1) {
        this.mTypeSupport = typeSupport
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (mTypeSupport != null) {
            // Need more layout
            mLayoutId = viewType
        }
        // Create view
        val view = mInflater?.inflate(mLayoutId, parent, false)
        return ViewHolder(view!!)
    }

    override fun getItemViewType(position: Int): Int {
        // Multiple layout issues
        return mTypeSupport?.getLayoutId(mData[position], position) ?: super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind data
        bindData(holder, mData[position], position)

//        if (mItemClickListener != null) {
//            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(mData[position], position) }
//        }
//        // Long press click event
//        if (mItemLongClickListener != null) {
//            holder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(mData[position], position) }
//        }
        // Item click event
        mItemClickListener?.let {
            holder.itemView.setOnClickListener { mItemClickListener!!.onItemClick(mData[position], position) }
        }

        //Long press click event
        mItemLongClickListener?.let {
            holder.itemView.setOnLongClickListener { mItemLongClickListener!!.onItemLongClick(mData[position], position) }
        }
    }

    /**
     * Pass the necessary parameters
     *
     * @param holder
     * @param data
     * @param position
     */
    protected abstract fun bindData(holder: ViewHolder, data: T, position: Int)

    override fun getItemCount(): Int {
        return mData.size
    }

    fun setOnItemClickListener(itemClickListener: OnItemClickListener) {
        this.mItemClickListener = itemClickListener
    }

    fun setOnItemLongClickListener(itemLongClickListener: OnItemLongClickListener) {
        this.mItemLongClickListener = itemLongClickListener
    }
}
