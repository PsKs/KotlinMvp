package com.hazz.kotlinmvp.ui.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import com.hazz.kotlinmvp.R
import com.hazz.kotlinmvp.base.BaseFragment
import com.hazz.kotlinmvp.base.BaseFragmentAdapter
import com.hazz.kotlinmvp.utils.StatusBarUtil
import com.hazz.kotlinmvp.view.TabLayoutHelper
import kotlinx.android.synthetic.main.fragment_hot.*

/**
 * Created by xuhao on 2017/12/7.
 * desc: Discovery (same layout as the popular homepage)
 */
class DiscoveryFragment : BaseFragment() {

    private val tabList = ArrayList<String>()

    private val fragments = ArrayList<Fragment>()

    private var mTitle: String? = null

    companion object {
        fun getInstance(title: String): DiscoveryFragment {
            val fragment = DiscoveryFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            fragment.mTitle = title
            return fragment
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_hot

    override fun initView() {
        // Status bar transparency and spacing processing
        activity?.let { StatusBarUtil.darkMode(it) }
        activity?.let { StatusBarUtil.setPaddingSmart(it, toolbar) }

        tv_header_title.text = mTitle

        tabList.add("interest")
        tabList.add("classify")
        fragments.add(FollowFragment.getInstance("attention"))
        fragments.add(CategoryFragment.getInstance("classify"))

        /**
         * getSupportFragmentManager() Replace with getChildFragmentManager()
         */
        mViewPager.adapter = BaseFragmentAdapter(childFragmentManager, fragments, tabList)
        mTabLayout.setupWithViewPager(mViewPager)
        TabLayoutHelper.setUpIndicatorWidth(mTabLayout)
    }

    override fun lazyLoad() {
    }
}