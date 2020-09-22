package com.hazz.kotlinmvp

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃   God Bless
//    ┃　　　┃   The code has no bugs!
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛
/**
 * Created by xuhao on 2017/11/27.
 * desc: constant
 */
class Constants private constructor() {

    companion object {

        val BUNDLE_VIDEO_DATA = "video_data"
        val BUNDLE_CATEGORY_DATA = "category_data"

        // Tencent Bugly APP id
        val BUGLY_APPID = "176aad0d9e"

        // File name stored by sp
        val FILE_WATCH_HISTORY_NAME = "watch_history_file"  // Watch History
        val FILE_COLLECTION_NAME = "collection_file"        // File name of favorite video cache
    }
}