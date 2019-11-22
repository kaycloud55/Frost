package com.kaycloud.framework.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.LruCache

/**
 * author: jiangyunkai
 * Created_at: 2019-11-22
 */
object ImageUtils {


    private lateinit var memoryCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()

        val cacheSize = maxMemory / 8

        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String?, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }
    }

    /**
     * 计算图片解码缩放比例
     * @param reqHeight 目标高度
     * @param reqWidth 目标宽度
     */
    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqHeight) {

            val halfHeight = height / 2
            val halfWidth = width / 2

            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }
}