package com.hazz.kotlinmvp.net

/**
 * Created by xuhao on 2017/11/16.
 * Encapsulate the returned data
 */
class BaseResponse<T>(val code :Int,
                      val msg:String,
                      val data:T)