package com.hazz.kotlinmvp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseFragment
import com.hazz.kotlinmvp.showToast
import com.hazz.kotlinmvp.ui.activity.AboutActivity
import com.hazz.kotlinmvp.ui.activity.ProfileHomePageActivity
import com.hazz.kotlinmvp.ui.activity.WatchHistoryActivity
import com.hazz.kotlinmvp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.fragment_mine.*

/**
 * Created by xuhao on 2017/11/9.
 * Mine Fragment
 */
class MineFragment : BaseFragment(),View.OnClickListener {

    private var mTitle:String? =null

    companion object {
        fun getInstance(title:String): MineFragment {
            val fragment = MineFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int= R.layout.fragment_mine

    override fun initView() {
        // Status bar transparency and spacing processing
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        tv_view_homepage.setOnClickListener(this)
        iv_avatar.setOnClickListener(this)
        iv_about.setOnClickListener(this)

        tv_collection.setOnClickListener(this)
        tv_comment.setOnClickListener(this)

        tv_mine_message.setOnClickListener(this)
        tv_mine_attention.setOnClickListener(this)
        tv_mine_cache.setOnClickListener(this)
        tv_watch_history.setOnClickListener(this)
        tv_feedback.setOnClickListener(this)
    }

    override fun lazyLoad() {

    }

    override fun onClick(v: View?) {
        when{
            v?.id==R.id.iv_avatar|| v?.id==R.id.tv_view_homepage -> {
                val intent = Intent(activity, ProfileHomePageActivity::class.java)
                startActivity(intent)
            }
            v?.id==R.id.iv_about ->{
                val intent = Intent(activity, AboutActivity::class.java)
                startActivity(intent)
            }
            v?.id==R.id.tv_collection -> showToast("Favorites")
            v?.id==R.id.tv_comment -> showToast("Comment")
            v?.id==R.id.tv_mine_message -> showToast("My message")
            v?.id==R.id.tv_mine_attention -> showToast("My focus")
            v?.id==R.id.tv_mine_attention -> showToast("My cache")
            v?.id==R.id.tv_watch_history -> startActivity(Intent(activity,WatchHistoryActivity::class.java))
            v?.id==R.id.tv_feedback -> showToast("Feedback")

        }
    }
}