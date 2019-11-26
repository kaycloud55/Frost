package com.kaycloud.framework.image

import android.content.Context
import com.bumptech.glide.Glide
import com.kaycloud.framework.image.config.GlideImageConfig
import com.kaycloud.framework.util.Preconditions

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class GlideImageLoadStrategy : BaseImageLoaderStrategy<GlideImageConfig> {
    override fun loadImage(context: Context, config: GlideImageConfig) {
        Preconditions.checkNotNull(context, "Context is Required!!!")
        Preconditions.checkNotNull(config, "config is Required!!!")
        Preconditions.checkNotNull(config.imageView, "Context is Required!!!")

        Glide.with(context)
    }

    override fun clear(context: Context, config: GlideImageConfig) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
