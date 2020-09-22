package com.hazz.kotlinmvp.mvp.presenter

import com.hazz.kotlinmvp.base.BasePresenter
import com.hazz.kotlinmvp.mvp.contract.HomeContract
import com.hazz.kotlinmvp.mvp.model.HomeModel
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.net.exception.ExceptionHandle

/**
 * Created by xuhao on 2017/11/8.
 * Home Featured Presenter
 * (The data is a HomeBean composed of Banner data and a page of data.
 * Check the interface and analyze it to understand)
 */

class HomePresenter : BasePresenter<HomeContract.View>(), HomeContract.Presenter {


    private var bannerHomeBean: HomeBean? = null

    private var nextPageUrl:String? = null // After loading the banner data of the homepage + one page of data, nextPageUrl does not add

    private val homeModel: HomeModel by lazy {

        HomeModel()
    }

    /**
     * Get featured data on the homepage banner plus one page of data
     */
    override fun requestHomeData(num: Int) {
        // Check whether View is bound
        checkViewAttached()
        mRootView?.showLoading()
        val disposable = homeModel.requestHomeData(num)
                .flatMap { homeBean ->

                    // Filter out Banner2 (including advertisements, and other unwanted Types),
                    // and check the interface analysis for details
                    val bannerItemList = homeBean.issueList[0].itemList

                    bannerItemList.filter { item ->
                        item.type=="banner2"|| item.type=="horizontalScrollCard"
                    }.forEach{ item ->
                        // Remove item
                        bannerItemList.remove(item)
                    }

                    bannerHomeBean = homeBean // Record the first page as banner data

                    // Request the next page of data according to nextPageUrl
                    homeModel.loadMoreData(homeBean.nextPageUrl)
                }

                .subscribe({ homeBean->
                    mRootView?.apply {
                        dismissLoading()

                        nextPageUrl = homeBean.nextPageUrl
                        // Filter out Banner2 (including advertisements, and other unwanted Types),
                        // and check the interface analysis for details
                        val newBannerItemList = homeBean.issueList[0].itemList

                        newBannerItemList.filter { item ->
                            item.type == "banner2"||item.type == "horizontalScrollCard"
                        }.forEach{ item ->
                            // Remove item
                            newBannerItemList.remove(item)
                        }
                        // Re-assign the banner length
                        bannerHomeBean!!.issueList[0].count = bannerHomeBean!!.issueList[0].itemList.size

                        // Assigned filtered data + banner data
                        bannerHomeBean?.issueList!![0].itemList.addAll(newBannerItemList)

                        setHomeData(bannerHomeBean!!)
                    }

                }, { t ->
                    mRootView?.apply {
                        dismissLoading()
                        showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                    }
                })

        addSubscription(disposable)

    }

    /**
     * load more
     */
    override fun loadMoreData() {
         val disposable = nextPageUrl?.let {
             homeModel.loadMoreData(it)
                     .subscribe({ homeBean->
                         mRootView?.apply {
                             // Filter out Banner2 (including advertisements, and other unwanted Types),
                             // and check the interface analysis for details
                             val newItemList = homeBean.issueList[0].itemList

                             newItemList.filter { item ->
                                 item.type=="banner2"||item.type=="horizontalScrollCard"
                             }.forEach{ item ->
                                 // Remove item
                                 newItemList.remove(item)
                             }

                             nextPageUrl = homeBean.nextPageUrl
                             setMoreData(newItemList)
                         }

                     },{ t ->
                         mRootView?.apply {
                             showError(ExceptionHandle.handleException(t),ExceptionHandle.errorCode)
                         }
                     })
         }
        if (disposable != null) {
            addSubscription(disposable)
        }
    }
}
