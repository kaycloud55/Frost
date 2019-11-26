package com.kaycloud.frost.base.loading

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.github.ybq.android.spinkit.SpinKitView
import com.kaycloud.framework.ext.setGone
import com.kaycloud.framework.ext.setVisible
import com.kaycloud.frost.R

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class GlobalLoadingStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    retryTask: (() -> Unit)? = null
) : LinearLayout(context, attrs, defStyleAttr) {

    private val spinKitView: SpinKitView
    private val tvEmpty: TextView
    private val tvFailed: TextView

    init {
        orientation = VERTICAL
        gravity = Gravity.CENTER
        LayoutInflater.from(context).inflate(R.layout.layout_default_loading, this, true)
        spinKitView = findViewById(R.id.spin_kit)
        tvFailed = findViewById(R.id.tv_load_fail)
        tvEmpty = findViewById(R.id.tv_empty_tips)
    }

    fun setStatus(status: LoadStatusType) {
        when (status) {
            LoadStatusType.STATUS_LOADING -> {
                spinKitView.setVisible()
                tvEmpty.setGone()
                tvFailed.setGone()
            }
            LoadStatusType.STATUS_EMPTY_DATA -> {
                spinKitView.setGone()
                tvEmpty.setVisible()
                tvFailed.setGone()

            }
            LoadStatusType.STATUS_LOAD_SUCCESS -> {
                spinKitView.setGone()
                tvEmpty.setGone()
                tvFailed.setGone()

            }
            LoadStatusType.STATUS_LOAD_FAILED -> {
                spinKitView.setGone()
                tvEmpty.setGone()
                tvFailed.setVisible()
            }
        }
    }
}