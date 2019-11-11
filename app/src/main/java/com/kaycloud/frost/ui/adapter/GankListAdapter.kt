package com.kaycloud.frost.ui.adapter

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.frost.R
import com.kaycloud.frost.data.entity.GankItemEntity
import com.kaycloud.frost.ui.PhotoDetailActivity

/**
 * Created by kaycloud on 2019-07-17
 */
class GankListAdapter(layoutId: Int, data: List<GankItemEntity>, private val context: Context) :
    BaseQuickAdapter<GankItemEntity, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder?, itemEntity: GankItemEntity?) {
        val img = helper?.getView<ImageView>(R.id.iv_img)
        if (itemEntity != null) {
            if (itemEntity.url?.endsWith(".jpg") == true) {
                img?.let {
                    Glide.with(context)
                        .load(itemEntity.url)
                        .override(Target.SIZE_ORIGINAL)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(it)
                }
            }
            img?.setOnClickListener {
                context.startActivity(Intent(context, PhotoDetailActivity::class.java).apply {
                    putExtra("url", itemEntity.url)
                })
            }
        }
    }
}