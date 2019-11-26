package com.kaycloud.framework.image

import android.content.Context
import com.kaycloud.framework.image.config.ImageConfig

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 * 图片加载策略基础功能
 */
interface BaseImageLoaderStrategy<T : ImageConfig> {

    /**
     * 加载图片
     * @param context see [Context]
     * @param config 图片加载配置信息 see[ImageConfig]
     */
    fun loadImage(context: Context, config: T)

    /**
     * 停止加载/取消加载
     * @param context
     * @param config
     */
    fun clear(context: Context, config: T)
}