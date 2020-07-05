package com.kaycloud.frost.base.loading

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import okhttp3.internal.platform.Platform

/**
 * author: jiangyunkai
 * Created_at: 2019-11-26
 */
class LoadingManager {

    private lateinit var mAdapter: Adapter
    private var logger: Logger = Logger.DEFAULT

    companion object {

        var mDefault: LoadingManager? = null

        /**
         * 初始化方法
         */
        fun of(adapter: Adapter): LoadingManager {
            val loadingManager = LoadingManager()
            loadingManager.mAdapter = adapter
            return loadingManager
        }

        fun initDefault(adapter: Adapter) {
            getDefault().mAdapter = adapter
        }

        fun getDefault(): LoadingManager {
            if (mDefault == null) {
                synchronized(LoadingManager::class.java) {
                    if (mDefault == null) {
                        mDefault = LoadingManager()
                    }
                }
            }
            return mDefault!!
        }

        /**
         * @param wrapper 内容布局（或者是包裹了FrameLayout之后的content）
         */
        class Holder constructor(
            val adapter: Adapter,
            val context: Context,
            private val wrapper: ViewGroup
        ) {
            private var mRetryTask: (() -> Unit)? = null
            private var curState: LoadStatusType? = null
            private val mStatusViews = SparseArray<View>(4)
            private var mData: Any? = null
            private var mCurStatusView: View? = null

            fun withRetry(retryTask: () -> Unit): Holder {
                mRetryTask = retryTask
                return this
            }

            fun withData(data: Any): Holder {
                mData = data
                return this
            }


            private fun switchLoadingStatus(status: LoadStatusType) {
                if (curState == status) {
                    return
                }
                curState = status
                //第一次尝试复用
                var loadView = mStatusViews.get(status.value)
                if (loadView == null) {
                    //再次尝试复用
                    loadView = mCurStatusView
                }
                val view = adapter.getView(this, loadView, status)
                //需要remove/add
                if (view != mCurStatusView || wrapper.indexOfChild(view) < 0) {
                    if (mCurStatusView != null) {
                        wrapper.removeView(mCurStatusView)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.elevation = Float.MAX_VALUE
                    }
                    wrapper.addView(view)
                    val layoutParams = view.layoutParams
                    if (layoutParams != null) {
                        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                    }
                } else if (wrapper.indexOfChild(view) != wrapper.childCount - 1) {
                    view.bringToFront()
                }
                mCurStatusView = view
                mStatusViews.put(status.value, view)

            }

            fun showLoading() = switchLoadingStatus(LoadStatusType.STATUS_LOADING)
            fun showLoadSuccess() = switchLoadingStatus(LoadStatusType.STATUS_LOAD_SUCCESS)
            fun showLoadFailed() = switchLoadingStatus(LoadStatusType.STATUS_LOAD_FAILED)
            fun showEmpty() = switchLoadingStatus(LoadStatusType.STATUS_EMPTY_DATA)
            fun getData() = mData
            fun getWrapper() = wrapper

        }
    }

    /**
     * 禁止通过构造方法初始化
     */
    private fun LoadingManager() {}

    /**
     * 方便替换log
     */
    fun log(logger: Logger) {
        this.logger = logger
    }

    /**
     * 设置LoadingView要放置的位置
     * @param activity 放置在整个activity的布局范围内
     * @throws Exception adapter未初始化
     */
    fun wrap(activity: Activity): Holder {
        checkAdapter()
        val wrapper = activity.findViewById<ViewGroup>(android.R.id.content)
        return Holder(mAdapter, activity, wrapper)
    }

    /**
     * wrap the specific view
     * @param view to be wrapped
     */
    fun wrap(view: View): Holder {
        checkAdapter()
        val wrapper = FrameLayout(view.context).apply {
            layoutParams = view.layoutParams
        }
        //如果指定的view位置有父布局，就替换view的位置
        (view.parent as? ViewGroup)?.run {
            val index = indexOfChild(view)
            removeView(view)
            addView(wrapper, index)
        }
        val newLp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        //将内容view包裹到我们自定义的wrapper中
        wrapper.addView(view, newLp)
        return Holder(mAdapter, view.context, wrapper)
    }

    /**
     *
     */
    fun cover(view: View): Holder {
        return (view.parent as? ViewGroup)?.run {
            val wrapper = FrameLayout(view.context).also {
                addView(it, view.layoutParams)
            }
            Holder(mAdapter, view.context, wrapper)
        }
            ?: throw RuntimeException("view has no parent to com.kaycloud.frost.util.show gloading as cover!")
    }


    private fun checkAdapter() {
        if (!this::mAdapter.isInitialized) {
            logger.log("please init Adapter First!!!")
            throw Exception("please init Adapter First!!!")
        }
    }


    /**
     * 继承Adapter，提供LoadingView
     */
    abstract class Adapter {
        /**
         * @return LoadingStatusView
         */
        abstract fun getView(holder: Holder, loadView: View?, status: LoadStatusType): View
    }

    /**
     * 可动态替换
     */
    interface Logger {
        fun log(message: String)

        companion object {
            /** A [Logger] defaults output appropriate for the current platform. */
            @JvmField
            val DEFAULT: Logger = object : Logger {
                override fun log(message: String) {
                    Platform.get().log(Platform.INFO, message, null)
                }
            }
        }
    }


}




