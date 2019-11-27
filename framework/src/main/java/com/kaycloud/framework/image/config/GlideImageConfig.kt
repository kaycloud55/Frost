package com.kaycloud.framework.image.config

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.TransitionOptions
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class GlideImageConfig private constructor(
    url: String?,
    target: ImageView,
    val cacheStrategy: CacheStrategy = CacheStrategy.NONE,
    val fallback: Drawable? = null,
    val imageRadius: Int = 0, //图片圆角大小
    val blurValue: Int = 0, //高斯模糊程度
    val isCircle: Boolean = false, //是否裁剪为圆形
    val isClearMemory: Boolean = false, //清除内存缓存
    val isClearDiskCache: Boolean = false, //清除磁盘缓存
    val isCenterCrop: Boolean = false,
    val fitCenter: Boolean = false,
    val transition: TransitionOptions<*, in Drawable>? = null,
    val onlyRetriveFromCache: Boolean = false, //仅从缓存中加载，如果缓存中没有则直接失败（省流量模式）
    val skipMemoryCache: Boolean = false, //跳过内存缓存（图片验证码）
    val skipDiskCache: Boolean = false //跳过磁盘缓存（图片验证码）
) : ImageConfig(url, target) {

    companion object {
        inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }

    private constructor(builder: Builder) : this(
        builder.url,
        builder.imageView,
        cacheStrategy = builder.cacheStrategy,
        fallback = builder.fallback,
        imageRadius = builder.imageRadius,
        blurValue = builder.blurValue,
        isCircle = builder.isCircle,
        isClearMemory = builder.isClearMemory,
        isCenterCrop = builder.isCenterCrop,
        isClearDiskCache = builder.isClearDiskCache
    )

    class Builder {
        lateinit var imageView: ImageView
            private set
        var cacheStrategy: CacheStrategy = CacheStrategy.NONE
            private set
        var fallback: Drawable? = null
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
        var fitCenter: Boolean = false
            private set
        var transition: TransitionOptions<*, in Drawable>? = null
            private set

        fun fallback(fallback: Drawable) = apply { this.fallback = fallback }

        fun url(url: String?) = apply { this.url = url }

        fun into(imageView: ImageView) = apply { this.imageView = imageView }

        fun imageRadius(radius: Int) = apply { this.imageRadius = radius }

        fun blurValue(blur: Int) = apply { this.blurValue = blur }

        fun isCircle(isCircle: Boolean) = apply { this.isCircle = isCircle }

        fun clearMemoryCache() = apply { this.isClearMemory = true }

        fun clearDiskCache() = apply { this.isClearDiskCache = true }

        fun centerCrop() = apply { this.isCenterCrop = true }

        fun cacheStrategy(cacheStrategy: CacheStrategy) =
            apply { this.cacheStrategy = cacheStrategy }

        fun fitCenter() = apply { this.fitCenter = true }

        fun transition(transition: TransitionOptions<*, in Drawable>) =
            apply { this.transition = transition }

        fun build() = GlideImageConfig(this)
    }
}