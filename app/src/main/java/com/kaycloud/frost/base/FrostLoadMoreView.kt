package com.kaycloud.frost.base

import com.chad.library.adapter.base.loadmore.LoadMoreView
import com.kaycloud.frost.R

/**
 * Created by jiangyunkai on 2019/11/27
 */
class FrostLoadMoreView : LoadMoreView() {

    override fun getLayoutId(): Int = R.layout.layout_common_adapter_loading

    override fun getLoadingViewId(): Int = R.id.spin_kit

    override fun getLoadEndViewId(): Int = R.id.tv_load_fail

    override fun getLoadFailViewId(): Int = R.id.tv_load_fail

}