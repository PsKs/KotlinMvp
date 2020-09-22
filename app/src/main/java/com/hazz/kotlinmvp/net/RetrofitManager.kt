package com.hazz.kotlinmvp.net

import com.hazz.kotlinmvp.MyApplication
import com.hazz.kotlinmvp.api.ApiService
import com.hazz.kotlinmvp.api.UrlConstant
import com.hazz.kotlinmvp.utils.AppUtils
import com.hazz.kotlinmvp.utils.NetworkUtil
import com.hazz.kotlinmvp.utils.Preference
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Created by xuhao on 2017/11/16.
 *
 */
object RetrofitManager {

    val service: ApiService by lazy (LazyThreadSafetyMode.SYNCHRONIZED){
        getRetrofit().create(ApiService::class.java)
    }

    private var token:String by Preference("token","")

    /**
     * Set public parameters
     */
    private fun addQueryParameterInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val request: Request
            val modifiedUrl = originalRequest.url().newBuilder()
                    // Provide your custom parameter here
                    .addQueryParameter("udid", "d2807c895f0348a180148c9dfa6f2feeac0781b5")
                    .addQueryParameter("deviceModel", AppUtils.getMobileModel())
                    .build()
            request = originalRequest.newBuilder().url(modifiedUrl).build()
            chain.proceed(request)
        }
    }

    /**
     * Set header
     */
    private fun addHeaderInterceptor(): Interceptor {
        return Interceptor { chain ->
            val originalRequest = chain.request()
            val requestBuilder = originalRequest.newBuilder()
                    // Provide your custom header here
                    .header("token", token)
                    .method(originalRequest.method(), originalRequest.body())
            val request = requestBuilder.build()
            chain.proceed(request)
        }
    }

    /**
     * Set cache
     */
    private fun addCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtil.isNetworkAvailable(MyApplication.context)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
            }
            val response = chain.proceed(request)
            if (NetworkUtil.isNetworkAvailable(MyApplication.context)) {
                val maxAge = 0
                // When there is a network, set the cache timeout time to 0 hours,
                // which means that the cached data is not read, only useful for get,
                // and the post is not buffered
                response.newBuilder()
                        .header("Cache-Control", "public, max-age=$maxAge")
                        // Clear the header information, because if the server does not support it,
                        // it will return some interference information.
                        // If it is not cleared, the following will not take effect
                        .removeHeader("Retrofit")
                        .build()
            } else {
                // When there is no network, set the timeout to 4 weeks.
                // It is only useful for get, and the post is not buffered
                val maxStale = 60 * 60 * 24 * 28
                response.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .removeHeader("nyn")
                        .build()
            }
            response
        }
    }

    private fun getRetrofit(): Retrofit {
        // Get an instance of retrofit
        return Retrofit.Builder()
                .baseUrl(UrlConstant.BASE_URL)  // Configure yourself
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

    }

    private fun getOkHttpClient(): OkHttpClient {
        // Add a log interceptor to print all logs
        val httpLoggingInterceptor = HttpLoggingInterceptor()

        // You can set the level of request filtering, body, basic, headers
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // Set the size and location of the requested cache
        val cacheFile = File(MyApplication.context.cacheDir, "cache")
        val cache = Cache(cacheFile, 1024 * 1024 * 50) //50Mb 缓存的大小

        return OkHttpClient.Builder()
                .addInterceptor(addQueryParameterInterceptor())  // Parameter addition
                .addInterceptor(addHeaderInterceptor()) // token filter
//              .addInterceptor(addCacheInterceptor())
                .addInterceptor(httpLoggingInterceptor) // Log, see all request responsiveness
                .cache(cache)  // Add cache
                .connectTimeout(60L, TimeUnit.SECONDS)
                .readTimeout(60L, TimeUnit.SECONDS)
                .writeTimeout(60L, TimeUnit.SECONDS)
                .build()
    }
}
