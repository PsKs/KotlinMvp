package com.hazz.kotlinmvp.utils

import android.content.Context
import android.util.DisplayMetrics

/**
 * Created by xuhao on 2017/11/27.
 * desc:
 */

object DisplayManager {
    init {
    }

    private var displayMetrics: DisplayMetrics? = null

    private var screenWidth: Int? = null

    private var screenHeight: Int? = null

    private var screenDpi: Int? = null

    fun init(context: Context) {
        displayMetrics = context.resources.displayMetrics
        screenWidth = displayMetrics?.widthPixels
        screenHeight = displayMetrics?.heightPixels
        screenDpi = displayMetrics?.densityDpi
    }

    // UI image size
    private const val STANDARD_WIDTH = 1080
    private const val STANDARD_HEIGHT = 1920

    fun getScreenWidth(): Int? {
        return screenWidth
    }

    fun getScreenHeight(): Int? {
        return screenHeight
    }


    /**
     * The height of the problem in the incoming UI map, in pixels
     * @param size
     * @return
     */
    fun getPaintSize(size: Int): Int? {
        return getRealHeight(size)
    }

    /**
     * Enter the size of the UI image and output the actual px
     *
     * @param px size in ui diagram
     * @return
     */
    fun getRealWidth(px: Int): Int? {
        // ui image width
        return getRealWidth(px, STANDARD_WIDTH.toFloat())
    }

    /**
     * Enter the size of the UI image, output the actual px, the second parameter is the parent layout
     *
     * @param px          size in ui diagram
     * @param parentWidth The width of the parent view in the ui map
     * @return
     */
    fun getRealWidth(px: Int, parentWidth: Float): Int? {
        return (px / parentWidth * getScreenWidth()!!).toInt()
    }

    /**
     * Enter the size of the UI image and output the actual px
     *
     * @param px size in ui diagram
     * @return
     */
    fun getRealHeight(px: Int): Int? {
        // ui image width
        return getRealHeight(px, STANDARD_HEIGHT.toFloat())
    }

    /**
     * Enter the size of the UI image, output the actual px, the second parameter is the parent layout
     *
     * @param px           size in ui diagram
     * @param parentHeight The height of the parent view in the ui map
     * @return
     */
    fun getRealHeight(px: Int, parentHeight: Float): Int? {
        return (px / parentHeight * getScreenHeight()!!).toInt()
    }

    /**
     * dip to px
     * @param dipValue
     * @return int
     */
    fun dip2px(dipValue: Float): Int? {
        val scale = displayMetrics?.density
        return (dipValue * scale!! + 0.5f).toInt()
    }
}