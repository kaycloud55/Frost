package com.kaycloud.frost.module.toplist.ui.toplist

import android.content.Context
import android.content.Intent
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kaycloud.frost.R
import com.kaycloud.frost.module.image.PhotoDetailActivity
import com.kaycloud.frost.module.toplist.TopListActivity

/**
 * author: jiangyunkai
 * Created_at: 2020-01-03
 */
class TopListCategoryAdapter(layoutId: Int, val context: Context) :
    BaseQuickAdapter<TopListCategory, BaseViewHolder>(layoutId) {
    override fun convert(helper: BaseViewHolder?, item: TopListCategory?) {
        val tvTitle = helper?.getView<TextView>(R.id.tv_title)
        tvTitle?.let {
            it.text = item?.title
        }


    }

}