package com.kaycloud.framework.image.config

import android.widget.ImageView
import okhttp3.internal.cache.CacheStrategy

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class GlideImageConfig private constructor(
    url: String?,
    target: ImageView?,
    val fallback: String? = null,
    val imageRadius: Int = 0, //图片圆角大小
    val blurValue: Int = 0, //高斯模糊程度
    val isCircle: Boolean = false, //是否裁剪为圆形
    val isClearMemory: Boolean = false, //清除内存缓存
    val isClearDiskCache: Boolean = false, //清除磁盘缓存
    val isCenterCrop: Boolean = false
) : ImageConfig(url, target) {

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    private constructor(builder: Builder) : this(builder.url, builder.imageView)

    class Builder {
        var imageView: ImageView? = null
            private set
        var fallback: String? = null
            private set
        var url: String? = null
            private set
        var imageRadius: Int = 0
            private set
        var blurValue: Int = 0 //高斯模糊程度
            private set
        var isCircle: Boolean = false //是否裁剪为圆形
            private set
        var isClearMemory: Boolean = false //清除内存缓存
            private set
        var isClearDiskCache: Boolean = false //清除磁盘缓存
            private set
        var isCenterCrop: Boolean = false
            private set

        fun fallback(fallback: String) = apply { this.fallback = fallback }

        fun url(url: String) = apply { this.url = url }

        fun into(imageView: ImageView) = apply { this.imageView = imageView }

        fun imageRadius(radius: Int) = apply { this.imageRadius = radius }

        fun blurValue(blur: Int) = apply { this.blurValue = blur }

        fun isCircle(isCircle: Boolean) = apply { this.isCircle = isCircle }

        fun isClearMemory(isClearMemory: Boolean) = apply { this.isClearMemory = isClearMemory }

        fun isClearDiskCache(isClearDiskCache: Boolean) = apply { this.isClearDiskCache = isCircle }

        fun isCenterCrop(isCircle: Boolean) = apply { this.isCenterCrop = isCircle }

        fun build() = GlideImageConfig(this)
    }
}