package com.kaycloud.framework.image

import android.content.Context
import com.kaycloud.framework.image.config.ImageConfig
import com.kaycloud.framework.util.Preconditions

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 * 封装图片加载类：
 *  1.获得对图片加载模块的控制权，如果以后要替换Glide，不要更改调用层代码
 *  2.方便扩展能力
 */
class ImageLoader {

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

    fun setLoadStrategy(strategy: BaseImageLoaderStrategy<ImageConfig>) {
        Preconditions.checkNotNull(mStrategy, "strategy == null")
        mStrategy = strategy
    }

    fun getLoadStrategy() = mStrategy

}