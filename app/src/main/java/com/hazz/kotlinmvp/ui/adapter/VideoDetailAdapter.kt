package com.hazz.kotlinmvp.ui.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.durationFormat
import com.hazz.kotlinmvp.glide.GlideApp
import com.hazz.kotlinmvp.glide.GlideRoundTransform
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.view.recyclerview.MultipleType
import com.hazz.kotlinmvp.view.recyclerview.ViewHolder
import com.hazz.kotlinmvp.view.recyclerview.adapter.CommonAdapter

/**
 * Created by xuhao on 2017/11/25.
 * desc: Video details
 */
class VideoDetailAdapter(mContext: Context, data: ArrayList<HomeBean.Issue.Item>) :
        CommonAdapter<HomeBean.Issue.Item>(mContext, data, object : MultipleType<HomeBean.Issue.Item> {
            override fun getLayoutId(item: HomeBean.Issue.Item, position: Int): Int {
                return when {
                    position == 0 ->
                        R.layout.item_video_detail_info

                    data[position].type == "textCard" ->
                        R.layout.item_video_text_card

                    data[position].type == "videoSmallCard" ->
                        R.layout.item_video_small_card
                    else ->
                        throw IllegalAccessException("Api parsing error, other types appear")
                }
            }
        }) {

    private var textTypeface:Typeface?=null

    init {
        textTypeface = Typeface.createFromAsset(MyApplication.context.assets, "fonts/FZLanTingHeiS-L-GB-Regular.TTF")
    }

    /**
     * Add video details
     */
    fun addData(item: HomeBean.Issue.Item) {
        mData.clear()
        notifyDataSetChanged()
        mData.add(item)
        notifyItemInserted(0)

    }

    /**
     * Add related recommendations and other data Item
     */
    fun addData(item: ArrayList<HomeBean.Issue.Item>) {
        mData.addAll(item)
        notifyItemRangeInserted(1, item.size)

    }

    /**
     * Kotlin functions can be used as parameters. When writing callbacks, you don’t need interface
     */
    private var mOnItemClickRelatedVideo: ((item: HomeBean.Issue.Item) -> Unit)? = null

    fun setOnItemDetailClick(mItemRelatedVideo: (item: HomeBean.Issue.Item) -> Unit) {
        this.mOnItemClickRelatedVideo = mItemRelatedVideo
    }

    /**
     * Bind data
     */
    override fun bindData(holder: ViewHolder, data: HomeBean.Issue.Item, position: Int) {
        when {
            position == 0 -> setVideoDetailInfo(data, holder)

            data.type == "textCard" -> {
                holder.setText(R.id.tv_text_card, data.data?.text!!)
                // Set Founder Lanting Fine Black Simplified
                holder.getView<TextView>(R.id.tv_text_card).typeface =textTypeface

            }
            data.type == "videoSmallCard" -> {
                with(holder) {
                    setText(R.id.tv_title, data.data?.title!!)
                    setText(R.id.tv_tag, "#${data.data.category} / ${durationFormat(data.data.duration)}")
                    setImagePath(R.id.iv_video_small_card, object : ViewHolder.HolderImageLoader(data.data.cover.detail) {
                            override fun loadImage(iv: ImageView, path: String) {
                                GlideApp.with(mContext)
                                        .load(path)
                                        .optionalTransform(GlideRoundTransform())
                                        .placeholder(R.drawable.placeholder_banner)
                                        .into(iv)
                            }
                        })
                }
                // Determine onItemClickRelatedVideo and use
                holder.itemView.setOnClickListener { mOnItemClickRelatedVideo?.invoke(data) }
            }
            else -> throw IllegalAccessException("Api parsing error, other types appear")
        }
    }

    /**
     * Set video details data
     */
    private fun setVideoDetailInfo(data: HomeBean.Issue.Item, holder: ViewHolder) {
        data.data?.title?.let { holder.setText(R.id.tv_title, it) }
        // Video Introduction
        data.data?.description?.let { holder.setText(R.id.expandable_text, it) }
        // label
        holder.setText(R.id.tv_tag, "#${data.data?.category} / ${durationFormat(data.data?.duration)}")
        // like
        holder.setText(R.id.tv_action_favorites, data.data?.consumption?.collectionCount.toString())
        // share it
        holder.setText(R.id.tv_action_share, data.data?.consumption?.shareCount.toString())
        // comment
        holder.setText(R.id.tv_action_reply, data.data?.consumption?.replyCount.toString())

        if (data.data?.author != null) {
            with(holder) {
                setText(R.id.tv_author_name, data.data.author.name)
                setText(R.id.tv_author_desc, data.data.author.description)
                setImagePath(R.id.iv_avatar, object : ViewHolder.HolderImageLoader(data.data.author.icon) {
                    override fun loadImage(iv: ImageView, path: String) {
                        //加载头像
                        GlideApp.with(mContext)
                                .load(path)
                                .placeholder(R.mipmap.default_avatar).circleCrop()
                                .into(iv)
                    }
                })
            }
        } else {
            holder.setViewVisibility(R.id.layout_author_view, View.GONE)
        }

        with(holder) {
            getView<TextView>(R.id.tv_action_favorites).setOnClickListener {
                Toast.makeText(MyApplication.context, "like", Toast.LENGTH_SHORT).show()
            }
            getView<TextView>(R.id.tv_action_share).setOnClickListener {
                Toast.makeText(MyApplication.context, "share it", Toast.LENGTH_SHORT).show()
            }
            getView<TextView>(R.id.tv_action_reply).setOnClickListener {
                Toast.makeText(MyApplication.context, "comment", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
