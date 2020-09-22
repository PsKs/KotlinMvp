package com.hazz.kotlinmvp.ui.activity

import android.support.v7.widget.LinearLayoutManager
import com.hazz.kotlinmvp.Constants
import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseActivity
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.ui.adapter.WatchHistoryAdapter
import com.hazz.kotlinmvp.utils.Preference
import com.hazz.kotlinmvp.utils.StatusBarUtil
import com.hazz.kotlinmvp.utils.WatchHistoryUtils
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_recyclerview.*
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by xuhao on 2017/12/11.
 * desc: Watch History
 */

class WatchHistoryActivity : BaseActivity() {

    private var itemListData = ArrayList<HomeBean.Issue.Item>()

    companion object {
        private const val HISTORY_MAX = 20
    }

    override fun layoutId(): Int = R.layout.layout_watch_history

    override fun initData() {
        multipleStatusView.showLoading()
        itemListData = queryWatchHistory()

    }

    override fun initView() {
        // return
        toolbar.setNavigationOnClickListener { finish() }

        val mAdapter = WatchHistoryAdapter(this, itemListData, R.layout.item_video_small_card)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mRecyclerView.adapter = mAdapter

        if (itemListData.size > 0) {
            multipleStatusView.showContent()
        } else {
            multipleStatusView.showEmpty()
        }

        // Status bar transparency and spacing processing
        StatusBarUtil.darkMode(this)
        StatusBarUtil.setPaddingSmart(this, toolbar)
        StatusBarUtil.setPaddingSmart(this, mRecyclerView)

    }

    override fun start() {

    }

    /**
     * Query viewing history
     */
    private fun queryWatchHistory(): ArrayList<HomeBean.Issue.Item> {
        val watchList = ArrayList<HomeBean.Issue.Item>()
        val hisAll = WatchHistoryUtils.getAll(Constants.FILE_WATCH_HISTORY_NAME, MyApplication.context) as Map<*, *>
        // Sort keys in ascending order
        val keys = hisAll.keys.toTypedArray()
        Arrays.sort(keys)
        val keyLength = keys.size
        // Calculate here. If the number of historical records is greater than the maximum number that can be displayed,
        // the maximum number is used as the loop condition to prevent the number of historical records-the maximum number is negative and the array is out of bounds
        val hisLength = if (keyLength > HISTORY_MAX) HISTORY_MAX else keyLength
        // Deserialization and traversal add viewing history
        (1..hisLength).mapTo(watchList) {
            WatchHistoryUtils.getObject(Constants.FILE_WATCH_HISTORY_NAME, MyApplication.context,
                    keys[keyLength - it] as String) as HomeBean.Issue.Item
        }
        
        return watchList
    }
}
