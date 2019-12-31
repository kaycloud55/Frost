package com.kaycloud.framework.image

import android.content.Context
import android.widget.ImageView
import com.kaycloud.framework.image.config.ImageConfig

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 * 图片加载策略基础功能
 * @param T 它对应的ImageConfig，可以是各种类型的，但都是ImageConfig的子类
 *  从逻辑定义上，想要达到的效果是，我声明一个 [BaseImageLoaderStrategy[ImageConfig]]，
 *  然后我想要传入一个[BaseImageLoaderStrategy[GlideImageConfig]]，这种是协变，要使用out.但是对于协变，
 *  又是不允许用在参数位置的，因为对于调用的方法而言，我本来想要的是一个[ImageConfig]，但是你实际上给我的是一个
 *  [GlideImageConfig]，可能方法都被覆盖了，调用就是不安全的。
 */

interface BaseImageLoaderStrategy<out T> {

    /**
     * 加载图片
     * @param context see [Context]
     * @param config 图片加载配置信息 see[ImageConfig]
     */
    fun loadImage(context: Context, config: @UnsafeVariance T)


    /**
     * 加载图片
     */
    fun loadImage(context: Context, imageView: ImageView, url: String?)


    /**
     * 停止加载/取消加载
     * @param context
     * @param config
     */
    fun clear(context: Context, config: @UnsafeVariance T)
}