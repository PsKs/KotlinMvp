@file:Suppress("DEPRECATION")

package com.hazz.kotlinmvp.glide

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation

import java.security.MessageDigest

/**
 * Created by xuhao on 2017/11/29.
 * desc:
 * 1. Never pass transform() to your original resource or original Bitmap to recycle(),
 *  let alone put it back into BitmapPool, because these are all done automatically.
 *  It is worth noting that any auxiliary Bitmap used for custom image transformation taken from
 *  BitmapPool must be actively put back into BitmapPool or recycled by calling recycle() if it is not returned by the transform() method.
 *
 * 2. If you take out multiple Bitmaps from BitmapPool or do not use one Bitmap that you take out from BitmapPool, you must return extras to BitmapPool.
 *
 * 3. If your image processing does not replace the original resource
 *  (for example, because an image has matched the size you want, you need to return in advance),
 *  the transform()` method will return the original resource or the original Bitmap.
 *
 */

class GlideRoundTransform @JvmOverloads constructor(dp: Int = 4) : BitmapTransformation() {

    private var radius = 0f

    init {
        this.radius = Resources.getSystem().displayMetrics.density * dp
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap? {
        return roundCrop(pool, toTransform)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {

    }

    private fun roundCrop(pool: BitmapPool, source: Bitmap?): Bitmap? {
        if (source == null) return null

        var result: Bitmap? = pool.get(source.width, source.height, Bitmap.Config.ARGB_8888)
        if (result == null) {
            result = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        }

        val canvas = Canvas(result!!)
        val paint = Paint()
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        paint.isAntiAlias = true
        val rectF = RectF(0f, 0f, source.width.toFloat(), source.height.toFloat())
        canvas.drawRoundRect(rectF, radius, radius, paint)
        return result
    }
}
