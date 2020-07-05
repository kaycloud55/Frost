package com.kaycloud.frost.base

import android.view.View
import androidx.fragment.app.Fragment
import com.kaycloud.frost.base.loading.DefaultLoadAdapter
import com.kaycloud.frost.base.loading.LoadingManager

/**
 * Created by jiangyunkai on 2019/11/25
 */
abstract class BaseFragment : Fragment() {

    private var mHolder: LoadingManager.Companion.Holder? = null

    protected fun getLoadingHolder() = mHolder

    protected fun initLoading(view: View) {
        if (mHolder == null) {
            mHolder = LoadingManager.of(DefaultLoadAdapter()).wrap(view)
                .withRetry {
                    onLoadRetry()
                }
        }
    }

    open fun onLoadRetry() {

    }

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