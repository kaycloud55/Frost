package com.kaycloud.framework.image

import android.content.Context
import android.widget.ImageView
import com.kaycloud.framework.image.config.ImageConfig
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
    fun <T : ImageConfig> clear(context: Context, config: T) {
        Preconditions.checkNotNull(
            mStrategy,
            "Please implement BaseImageLoaderStrategy and " +
                    "call GlobalConfigModule.Builder#imageLoaderStrategy(BaseImageLoaderStrategy)" +
                    " in the applyOptions method of ConfigModule"
        )
        mStrategy?.clear(context, config)
    }

    fun <T : ImageConfig> setLoadStrategy(strategy: BaseImageLoaderStrategy<T>) {
        Preconditions.checkNotNull(strategy, "strategy == null")
        mStrategy = strategy
    }

    fun getLoadStrategy() = mStrategy

}