package com.kaycloud.frost.image.wallhaven.ui

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.Target
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.frost.R
import com.kaycloud.frost.image.wallhaven.data.WallhavenItemEntity
import com.kaycloud.frost.image.PhotoDetailActivity

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */
class WallhavenListAdapter(
    layoutId: Int,
    private val context: Context
) : BaseQuickAdapter<WallhavenItemEntity, BaseViewHolder>(layoutId, null) {

    override fun convert(helper: BaseViewHolder?, item: WallhavenItemEntity?) {
        val img = helper?.getView<ImageView>(R.id.iv_img)
        item?.let {
            img?.apply {
                Glide.with(context)
                    .load(it.thumbs.original)
                    .override(Target.SIZE_ORIGINAL)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(this)
            }
            img?.setOnClickListener {
                context.startActivity(Intent(context, PhotoDetailActivity::class.java).apply {
                    putExtra("url", item.path)
                })
            }
        }
    }
}