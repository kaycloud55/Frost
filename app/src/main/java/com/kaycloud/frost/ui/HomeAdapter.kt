package com.kaycloud.frost.ui

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.frost.R
import com.kaycloud.frost.data.GankItem

/**
 * Created by kaycloud on 2019-07-17
 */
class HomeAdapter(layoutId: Int, data: List<GankItem>, private val context: Context) :
    BaseQuickAdapter<GankItem, BaseViewHolder>(layoutId, data) {

    override fun convert(helper: BaseViewHolder?, item: GankItem?) {
        val img = helper?.getView<ImageView>(R.id.iv_img)
        img?.let {
            Glide.with(context)
                .load(item?.url)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(it)
        }

    }


}