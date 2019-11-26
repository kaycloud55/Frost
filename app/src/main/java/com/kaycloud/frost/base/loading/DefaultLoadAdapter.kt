package com.kaycloud.frost.base.loading

import android.view.View

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class DefaultLoadAdapter : LoadingManager.Adapter() {

    override fun getView(
        holder: LoadingManager.Companion.Holder,
        loadView: View?,
        status: LoadStatusType
    ): View {
        val loadingStatusView: GlobalLoadingStatusView =
            loadView as? GlobalLoadingStatusView ?: GlobalLoadingStatusView(holder.context)
        loadingStatusView.setStatus(status)
        return loadingStatusView
    }
}