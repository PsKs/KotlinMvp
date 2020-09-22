package com.hazz.kotlinmvp.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.R.id.toolbar
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.utils.AppUtils
import com.hazz.kotlinmvp.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_about.*

/**
 * Created by xuhao on 2017/12/6.
 * desc: about
 */
class AboutActivity : BaseActivity() {
    override fun layoutId(): Int = R.layout.activity_about

    override fun initData() {
    }

    @SuppressLint("SetTextI18n")
    override fun initView() {
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)

        tv_version_name.text ="v${AppUtils.getVerName(MyApplication.context)}"
        // return back
        toolbar.setNavigationOnClickListener { finish() }
        // visit GitHub
        relayout_gitHub.setOnClickListener {
            val uri = Uri.parse("https://github.com/PsKs/KotlinMvp")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
    }

    override fun start() {

    }
}
