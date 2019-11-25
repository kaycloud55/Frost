package com.kaycloud.frost.base

import android.os.Bundle
import androidx.fragment.app.Fragment

/**
 * Created by jiangyunkai on 2019/11/25
 */
abstract class BaseFragment : Fragment() {

    private var mHolder: Gloading.Holder? = null

    protected fun shouldInitLoadingStatusView(): Boolean = true

    private fun initLoadingStatusView() {
        if (mHolder == null) {
            mHolder = Gloading.getDefault().wrap(activity).withRetry(onLoadRetry)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (shouldInitLoadingStatusView()) {

        }
    }

    abstract var onLoadRetry: () -> Unit

    fun showLoading() {
        mHolder?.showLoading()
    }

    fun showLoadFailed() {
        mHolder?.showLoadFailed()
    }

    fun showContent() {
        mHolder?.showLoadSuccess()
    }

    fun showEmpty() {
        mHolder?.showEmpty()
    }

}