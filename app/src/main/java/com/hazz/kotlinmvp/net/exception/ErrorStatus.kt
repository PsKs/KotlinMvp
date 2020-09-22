package com.hazz.kotlinmvp.net.exception

/**
 * Created by xuhao on 2017/12/5.
 * desc:
 */
object ErrorStatus {
    /**
     * Successful response
     */
    @JvmField
    val SUCCESS = 0

    /**
     * unknown error
     */
    @JvmField
    val UNKNOWN_ERROR = 1002

    /**
     * Server internal error
     */
    @JvmField
    val SERVER_ERROR = 1003

    /**
     * Network connection timed out
     */
    @JvmField
    val NETWORK_ERROR = 1004

    /**
     * API parsing exception (or third-party data structure change) and other exceptions
     */
    @JvmField
    val API_ERROR = 1005

}