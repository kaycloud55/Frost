package com.kaycloud.frost.module.image.wallhaven.ui

import android.content.Context
import android.content.Intent
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.framework.image.imageloader.ImageLoader
import com.kaycloud.frost.R
import com.kaycloud.frost.module.image.wallhaven.data.WallhavenItemEntity
import com.kaycloud.frost.module.image.PhotoDetailActivity

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
                ImageLoader.loadImage(context, this, it.thumbs.original)
            }
            img?.setOnClickListener {
                context.startActivity(Intent(context, PhotoDetailActivity::class.java).apply {
                    putExtra("url", item.path)
                })
            }
        }
    }
}