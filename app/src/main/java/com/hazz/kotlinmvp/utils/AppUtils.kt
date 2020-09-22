package com.hazz.kotlinmvp.utils

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import java.security.MessageDigest

/**
 * Created by xuhao on 2017/12/6.
 * desc: APP Related tools
 */

class AppUtils private constructor() {

    init {
        throw Error("Do not need instantiate!")
    }

    companion object {

        private val DEBUG = true
        private val TAG = "AppUtils"

        /**
         * Get the software version number
         *
         * @param context Context
         * @return Current version Code
         */
        fun getVerCode(context: Context): Int {
            var verCode = -1
            try {
                val packageName = context.packageName
                verCode = context.packageManager
                        .getPackageInfo(packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verCode
        }

        /**
         * Get the maximum memory used by the application
         *
         * @return Maximum memory
         */
        val maxMemory: Long
            get() = Runtime.getRuntime().maxMemory() / 1024


        /**
         * Get software display version information
         *
         * @param context context
         * @return Current version information
         */
        fun getVerName(context: Context): String {
            var verName = ""
            try {
                val packageName = context.packageName
                verName = context.packageManager
                        .getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verName
        }


        @SuppressLint("PackageManagerGetSignatures")
        /**
         * Get app signature
         *
         * @param context context
         * @param pkgName Package names
         * @return Return the signature of the application
         */
        fun getSign(context: Context, pkgName: String): String? {
            return try {
                @SuppressLint("PackageManagerGetSignatures") val pis = context.packageManager
                        .getPackageInfo(pkgName,
                                PackageManager.GET_SIGNATURES)
                hexDigest(pis.signatures[0].toByteArray())
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
                null
            }
        }

        /**
         * Convert the signature string to the required 32-bit signature
         *
         * @param paramArrayOfByte Signature byte array
         * @return 32-bit signature string
         */
        private fun hexDigest(paramArrayOfByte: ByteArray): String {
            val hexDigits = charArrayOf(48.toChar(), 49.toChar(), 50.toChar(), 51.toChar(), 52.toChar(), 53.toChar(), 54.toChar(), 55.toChar(), 56.toChar(), 57.toChar(), 97.toChar(), 98.toChar(), 99.toChar(), 100.toChar(), 101.toChar(), 102.toChar())
            try {
                val localMessageDigest = MessageDigest.getInstance("MD5")
                localMessageDigest.update(paramArrayOfByte)
                val arrayOfByte = localMessageDigest.digest()
                val arrayOfChar = CharArray(32)
                var i = 0
                var j = 0
                while (true) {
                    if (i >= 16) {
                        return String(arrayOfChar)
                    }
                    val k = arrayOfByte[i].toInt()
                    arrayOfChar[j] = hexDigits[0xF and k.ushr(4)]
                    arrayOfChar[++j] = hexDigits[k and 0xF]
                    i++
                    j++
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return ""
        }

        /**
         * Get the available memory size of the device
         *
         * @param context Application context object context
         * @return Current memory size
         */
        fun getDeviceUsableMemory(context: Context): Int {
            val am = context.getSystemService(
                    Context.ACTIVITY_SERVICE) as ActivityManager
            val mi = ActivityManager.MemoryInfo()
            am.getMemoryInfo(mi)
            // Return the available memory of the current system
            return (mi.availMem / (1024 * 1024)).toInt()
        }

        fun getMobileModel(): String {
            var model: String? = Build.MODEL
            model = model?.trim { it <= ' ' } ?: ""
            return model
        }

        /**
         * Get the mobile phone system SDK version
         *
         * @return If API 17 returns 17
         */
        val sdkVersion: Int
            get() = android.os.Build.VERSION.SDK_INT
    }
}