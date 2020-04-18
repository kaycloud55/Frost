package com.kaycloud.framework.image.myloader

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.util.LruCache
import android.widget.ImageView
import com.kaycloud.framework.R
import com.kaycloud.framework.executor.AppTaskExecutor
import com.kaycloud.framework.ext.TAG
import com.kaycloud.framework.image.myloader.cache.DiskLruCache
import com.kaycloud.framework.image.myloader.compress.ImageCompressor
import com.kaycloud.framework.log.KLog
import java.io.FileInputStream
import java.io.IOException
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

/**
 * Created by jiangyunkai on 2020/4/13
 *
 * 一般来说，一个优秀的ImageLoader应该具有如下功能：
 *  1.图片的同步加载
 *  2.图片的异步架子啊
 *  3.图片压缩
 *  4.内存缓存
 *  5.磁盘缓存
 *  6.网络拉取
 */

internal class MyImageLoader(context: Context) {

    companion object {
        private const val DISK_CACHE_SIZE = 1024 * 1024 * 10L //10MB
        private const val DISK_CACHE_SUB_DIR = "thumbnails"

        private const val TAG_KEY_URI = 0x111111

        private const val DISK_CACHE_INDEX = 0

        private const val TAG = "ImageLoader"
    }

    private lateinit var memoryCache: LruCache<String, Bitmap>
    private var diskLruCache: DiskLruCache? = null
    private val diskCacheLock = ReentrantLock()
    private val diskCacheLockCondition: Condition = diskCacheLock.newCondition()
    private var diskCacheStarting = true

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        memoryCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, value: Bitmap): Int {
                return value.byteCount / 1024
            }
        }

        val diskCacheDir = CacheUtil.getDiskCacheDir(
            context,
            DISK_CACHE_SUB_DIR
        )
        if (!diskCacheDir.exists()) {
            diskCacheDir.mkdirs()
        }

        if (CacheUtil.getAvailableSpace(diskCacheDir) > DISK_CACHE_SIZE) {
            try {
                diskLruCache = DiskLruCache.open(
                    diskCacheDir, 1, 1,
                    DISK_CACHE_SIZE
                )
                diskCacheStarting = false
            } catch (e: IOException) {
                KLog.e(TAG, "init ImageLoader failed.error:$e")
            }
        }
    }


    fun addBitmapToMemCache(key: String, bitmap: Bitmap) {
        if (getBitmapFromDiskCache(key) == null) {
            memoryCache.put(key, bitmap)
        }
    }

    fun bindBitmap(url: String, imageView: ImageView) {
        bindBitmap(url, imageView, 0, 0)
    }


    fun bindBitmap(url: String, imageView: ImageView, reqWidth: Int, reqHeight: Int) {
        imageView.setTag(TAG_KEY_URI, url)
        var bitmap = loadBitmapFromMemCache(url)
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap)
            return
        }

        AppTaskExecutor.getInstance().execute {
            val bitmapFromCache = loadBitmap(url, reqWidth, reqHeight)
            if (bitmapFromCache != null) {
                imageView.setImageBitmap(bitmapFromCache)
            }
        }
    }

    private fun loadBitmap(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        var bitmap = loadBitmapFromMemCache(url)
        if (bitmap != null) {
            KLog.d(TAG, "loadBitmapFromMemCache, url:$url")
            return bitmap
        }
        try {
            bitmap = loadBitmapFromDiskCache(url, reqWidth, reqHeight)
            if (bitmap != null) {
                KLog.d(Companion.TAG, "loadBitmapFromDisk, url: $url")
                return bitmap
            }
            bitmap = loadBitmapFromNetworkCache(url, reqWidth, reqHeight)
            KLog.d(Companion.TAG, "loadBitmapFromNetwork, url:$url")
        } catch (e: IOException) {
            KLog.e(Companion.TAG, "loadBitmap failed.")
        }
        if (bitmap == null && diskCacheStarting) {
            KLog.w(Companion.TAG, "encounter error, diskLruCache is not created.")
            bitmap = downloadBitmapFromUrl(url)
        }
        return bitmap
    }

    private fun loadBitmapFromMemCache(url: String): Bitmap? {
        val key = CacheUtil.hashKeyFromUrl(url)
        return memoryCache.get(key)
    }


    private fun loadBitmapFromDiskCache(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {

        if (diskLruCache == null) {
            return null
        }

        var bitmap: Bitmap? = null
        val key = CacheUtil.hashKeyFromUrl(url)
        val snapshot = diskLruCache?.get(key)
        if (snapshot != null) {
            val inputStream = snapshot.getInputStream(DISK_CACHE_INDEX) as FileInputStream
            val fd = inputStream.fd
            bitmap = ImageCompressor.getInstance()
                .decodeSampleBitmapFromFileDescriptor(fd, Rect(), reqWidth, reqHeight)
            if (bitmap != null) {
                addBitmapToMemCache(key, bitmap)
            }
        }
        return bitmap
    }

    private fun loadBitmapFromNetworkCache(url: String, reqWidth: Int, reqHeight: Int): Bitmap? {
        return null
    }

    private fun downloadBitmapFromUrl(url: String): Bitmap? {
        return null


    }


    fun getBitmapFromDiskCache(key: String): Bitmap? =
        diskCacheLock.withLock {
            // 自旋到后台线程初始化DiskLruCache
            while (diskCacheStarting) {
                try {
                    diskCacheLockCondition.await()
                } catch (e: InterruptedException) {

                }
            }
//            return diskLruCache?.get(key)
            return@withLock null
        }


}


