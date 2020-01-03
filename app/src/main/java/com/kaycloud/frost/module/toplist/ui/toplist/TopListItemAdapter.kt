package com.kaycloud.frost.module.toplist.ui.toplist

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.frost.R
import com.kaycloud.frost.module.image.PhotoDetailActivity

/**
 * author: jiangyunkai
 * Created_at: 2020-01-03
 */
class TopListItemAdapter(layoutId: Int, val context: Context) :
    BaseQuickAdapter<ToplistItem, BaseViewHolder>(layoutId) {
    override fun convert(helper: BaseViewHolder?, item: ToplistItem?) {
        val tvTitle = helper?.getView<TextView>(R.id.tv_title)
        tvTitle?.let {
            it.text = item?.Title
        }
        setOnItemChildClickListener { adapter, view, position ->
            context.startActivity(Intent(context, PhotoDetailActivity::class.java).apply {
                putExtra("url", item?.Url)
            })
        }

    }

}