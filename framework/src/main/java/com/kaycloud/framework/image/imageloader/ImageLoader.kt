package com.kaycloud.framework.image.imageloader

import android.content.Context
import android.widget.ImageView
import com.kaycloud.framework.image.imageloader.config.ImageConfig
import com.kaycloud.framework.util.Preconditions

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 * 封装图片加载类：
 *  1.获得对图片加载模块的控制权，如果以后要替换Glide，不要更改调用层代码
 *  2.方便扩展能力
 */
object ImageLoader {

//    companion object {
//        private val instance = ImageLoaderHolder.imageLoader
//
//        fun getInstance() = instance
//    }
//
//    private object ImageLoaderHolder {
//        val imageLoader = ImageLoader()
//    }

    /**
     * 从类型上来说，这里期望的是BaseImageLoaderStrategy<ImageConfig>，如果泛型不声明成out的话，
     * 赋值GlideImageLoaderStrategy<GlideImageConfig>就不行了，因为它不是子类型。
     *
     * BaseImageLoaderStrategy<Glide> 是 BaseImageLoaderStrategy<ImageConfig>的子类型
     * GlideImageLoader是BaseImageLoaderStrategy的子类型，GlideImageLoader<Glide>是BaseImageLoaderStrategy<Glide>的子类型，
     * 所以GlideImageLoader<Glide>是BaseImageLoaderStrategy<ImageConfig>的子类型
     *
     */
    private var mStrategy: BaseImageLoaderStrategy<ImageConfig>? = null

    /**
     * 加载图片
     * @param config
     * @param context
     * @param T
     */
    fun <T : ImageConfig> loadImage(context: Context, config: T) {
        Preconditions.checkNotNull(
            mStrategy,
            "Please implement BaseImageLoaderStrategy and " +
                    "call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy)" +
                    " in the applyOptions method of ConfigModule"
        )
        mStrategy?.loadImage(context, config)
    }


    /**
     * 加载图片
     * @param config
     * @param context
     */
    fun loadImage(context: Context, imageView: ImageView, url: String?) {
        Preconditions.checkNotNull(
            mStrategy,
            "Please implement BaseImageLoaderStrategy and " +
                    "call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy)" +
                    " in the applyOptions method of ConfigModule"
        )
        mStrategy?.loadImage(context, imageView, url)
    }


    /**
     * 停止加载或清理缓存
     * @param config
     * @param context
     * @param T
     */
    fun clear(context: Context, config: ImageConfig) {
        Preconditions.checkNotNull(
            mStrategy,
            "Please implement BaseImageLoaderStrategy and " +
                    "call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy)" +
                    " in the applyOptions method of ConfigModule"
        )
        mStrategy?.clear(context, config)
    }

    /**
     * 由于BaseImageLoaderStrategy类型形参说明在了out位置，所以这里传入类型实参的时候，可以是ImageConfig的子类
     */
    fun setLoadStrategy(strategy: BaseImageLoaderStrategy<ImageConfig>) {
        Preconditions.checkNotNull(strategy, "strategy == null")
        mStrategy = strategy
    }

    fun getLoadStrategy() =
        mStrategy

}