package com.hazz.kotlinmvp.glide

import android.content.Context

import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.module.AppGlideModule

import java.io.InputStream

/**
 * Created by xuhao on 2017/12/1.
 * desc:
 */
@GlideModule
class CustomAppGlideModule : AppGlideModule() {

    /**
     * Set the default structure (Engine, BitmapPool, ArrayPool, MemoryCache, etc.) through GlideBuilder.
     *
     * @param context
     * @param builder
     */
    override fun applyOptions(context: Context, builder: GlideBuilder) {

        //Reset memory limit
        builder.setMemoryCache(LruResourceCache(10 * 1024 * 1024))

    }

    /**
     * Open list analysis
     *
     *
     * Do not open here to avoid adding the same modules twice
     *
     * @return
     */
    override fun isManifestParsingEnabled(): Boolean {
        return false
    }

    /**
     *
     * Register a custom String BaseGlideUrlLoader for the App
     * @param context
     * @param glide
     * @param registry
     */
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.append(String::class.java, InputStream::class.java, CustomBaseGlideUrlLoader.Factory())
    }
}
