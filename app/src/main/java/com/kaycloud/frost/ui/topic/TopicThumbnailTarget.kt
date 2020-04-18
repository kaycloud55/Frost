package com.kaycloud.frost.ui.topic

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.Px
import com.bumptech.glide.request.target.ImageViewTarget

/**
 * Created by jiangyunkai on 2020/4/13
 */
class TopicThumbnailTarget(
    imageView: ImageView,
    @ColorInt private val selectedTint: Int,
    @Px private val selectedTopLeftCornerRadius: Int,
    private val selectedDrawable: Drawable
) : ImageViewTarget<Bitmap>(imageView) {

    override fun setResource(resource: Bitmap?) {
        resource?.let {
            val d = (currentDrawable as? TopicThumbnailDrawable) ?: TopicThumbnailDrawable(
                selectedTint,
                selectedTopLeftCornerRadius,
                selectedDrawable
            )
            d.bitmap = it
            setDrawable(d)
        }
    }
}