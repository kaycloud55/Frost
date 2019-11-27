package com.kaycloud.frost.module.image.gank.ui

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.framework.image.ImageLoader
import com.kaycloud.frost.R
import com.kaycloud.frost.module.image.gank.data.GankItemEntity
import com.kaycloud.frost.module.image.PhotoDetailActivity

/**
 * Created by kaycloud on 2019-07-17
 */
class GankListAdapter(layoutId: Int, data: List<GankItemEntity>, private val context: Context) :
    BaseQuickAdapter<GankItemEntity, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder?, itemEntity: GankItemEntity?) {
        val imageView = helper?.getView<ImageView>(R.id.iv_img)
        imageView?.let {
            if (itemEntity == null) {
                Glide.with(context).clear(imageView)
            } else {
                ImageLoader.loadImage(context, it, itemEntity.url)

                it.setOnClickListener {
                    context.startActivity(Intent(context, PhotoDetailActivity::class.java).apply {
                        putExtra("url", itemEntity.url)
                    })
                }
            }
        }
    }

}