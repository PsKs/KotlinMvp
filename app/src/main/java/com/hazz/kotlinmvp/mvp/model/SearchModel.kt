package com.hazz.kotlinmvp.mvp.model

import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.net.RetrofitManager
import com.hazz.kotlinmvp.rx.scheduler.SchedulerUtils
import io.reactivex.Observable

/**
 * Created by xuhao on 2017/11/30.
 * desc: search for Model
 */
class SearchModel {

    /**
     * Request data for popular keywords
     */
    fun requestHotWordData(): Observable<ArrayList<String>> {

        return RetrofitManager.service.getHotWord()
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * Results returned by search keywords
     */
    fun getSearchResult(words: String):Observable<HomeBean.Issue>{
        return RetrofitManager.service.getSearchData(words)
                .compose(SchedulerUtils.ioToMain())
    }

    /**
     * Load more data
     */
    fun loadMoreData(url: String): Observable<HomeBean.Issue> {
        return RetrofitManager.service.getIssueData(url)
                .compose(SchedulerUtils.ioToMain())
    }
}
