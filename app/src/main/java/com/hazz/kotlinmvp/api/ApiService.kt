package com.hazz.kotlinmvp.api

import com.hazz.kotlinmvp.mvp.model.bean.AuthorInfoBean
import com.hazz.kotlinmvp.mvp.model.bean.CategoryBean
import com.hazz.kotlinmvp.mvp.model.bean.HomeBean
import com.hazz.kotlinmvp.mvp.model.bean.TabInfoBean
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Created by xuhao on 2017/11/16.
 * Api interface
 */

interface ApiService{

    /**
     * Home Featured
     */
    @GET("v2/feed?")
    fun getFirstHomeData(@Query("num") num:Int): Observable<HomeBean>

    /**
     * According to nextPageUrl request data next page data
     */
    @GET
    fun getMoreHomeData(@Url url: String): Observable<HomeBean>

    /**
     * Get related videos according to item id
     */
    @GET("v4/video/related?")
    fun getRelatedData(@Query("id") id: Long): Observable<HomeBean.Issue>

    /**
     * Get classification
     */
    @GET("v4/categories")
    fun getCategory(): Observable<ArrayList<CategoryBean>>

    /**
     * Get category details list
     */
    @GET("v4/categories/videoList?")
    fun getCategoryDetailList(@Query("id") id: Long): Observable<HomeBean.Issue>

    /**
     * Get more issues
     */
    @GET
    fun getIssueData(@Url url: String): Observable<HomeBean.Issue>

    /**
     * Get Info of all leaderboards (including title and Url)
     */
    @GET("v4/rankList")
    fun getRankList():Observable<TabInfoBean>

    /**
     * Get search information
     */
    @GET("v1/search?&num=10&start=10")
    fun getSearchData(@Query("query") query :String) : Observable<HomeBean.Issue>

    /**
     * Popular search terms
     */
    @GET("v3/queries/hot")
    fun getHotWord():Observable<ArrayList<String>>

    /**
     * follow
     */
    @GET("v4/tabs/follow")
    fun getFollowInfo():Observable<HomeBean.Issue>

    /**
     * author information
     */
    @GET("v4/pgcs/detail/tab?")
    fun getAuthorInfo(@Query("id") id: Long):Observable<AuthorInfoBean>



}