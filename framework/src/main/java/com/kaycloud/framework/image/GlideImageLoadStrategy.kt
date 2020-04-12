package com.kaycloud.framework.image

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.kaycloud.framework.executor.AppTaskExecutor
import com.kaycloud.framework.image.config.GlideImageConfig
import com.kaycloud.framework.util.Preconditions

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class GlideImageLoadStrategy : BaseImageLoaderStrategy<GlideImageConfig> {

    override fun loadImage(context: Context, imageView: ImageView, url: String?) {
        Glide.with(context).load(url).into(imageView)
    }

    override fun loadImage(context: Context, config: GlideImageConfig) {
        Preconditions.checkNotNull(context, "Context is Required!!!")
        Preconditions.checkNotNull(config, "config is Required!!!")
        Preconditions.checkNotNull(config.imageView, "Context is Required!!!")

        var requestBuilder = Glide.with(context).load(config.url)

        if (config.placeHolder != 0) {
            requestBuilder = requestBuilder.placeholder(config.placeHolder)
        }

        if (config.errorPic != 0) {
            requestBuilder = requestBuilder.error(config.errorPic)
        }

        if (config.fallback != null) {
            requestBuilder = requestBuilder.fallback(config.fallback)
        }

        if (config.isCircle) {
            requestBuilder = requestBuilder.circleCrop()
        }

        if (config.isCenterCrop) {
            requestBuilder = requestBuilder.centerCrop()
        }

        if (config.fitCenter) {
            requestBuilder = requestBuilder.fitCenter()
        }

        if (config.transition != null) {
            requestBuilder = requestBuilder.transition(config.transition)
        }

        requestBuilder.into(config.imageView)

    }

    override fun clear(context: Context, config: GlideImageConfig) {
        Preconditions.checkNotNull(context, "Context is Required!!!")
        Preconditions.checkNotNull(config, "config is Required!!!")
        Preconditions.checkNotNull(config.imageView, "Context is Required!!!")

        Glide.with(context).clear(config.imageView)

        if (config.isClearDiskCache) {
            AppTaskExecutor.getInstance().execute {

            }
        }

        if (config.isClearMemory) {
            AppTaskExecutor.getInstance().execute {
                //TODO:clear memory cache
            }
        }
    }

}
