package com.kaycloud.frost.module.image.wallhaven.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kaycloud.framework.ext.toast
import com.kaycloud.framework.log.KLog
import com.kaycloud.frost.R
import com.kaycloud.frost.base.BaseFragment
import com.kaycloud.frost.module.image.wallhaven.data.DisplayType
import com.kaycloud.frost.module.image.wallhaven.data.WallHavenSearchOptions
import com.kaycloud.frost.module.image.wallhaven.data.WallhavenViewModel
import com.kaycloud.frost.network.Status
import java.lang.Exception

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */

class WallhavenListFragment : BaseFragment() {

    private val TAG = "WallhavenListFragment"

    private var columnCount = 1

    private lateinit var viewModel: WallhavenViewModel

    private var mAdapter: WallhavenListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        //这里既是初始化，又需要返回值，所以使用run
        viewModel = activity?.run {
            ViewModelProviders.of(this)[WallhavenViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        viewModel.getSearchResult().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        mAdapter?.addData(it.data)
                        showContent()
                    } else {
                        activity?.toast("数据为空")
                        showEmpty()
                        KLog.i(TAG, "load success! data is null!!!!")
                    }
                }
                Status.ERROR -> {
                    activity?.toast("加载错误")
                    KLog.i(TAG, "load error:${it.message}")
                    showLoadFailed()
                }
                Status.LOADING -> {
                    showLoading()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_wallhavenitementity_list, container, false)
        initLoading(view)
        // Set the adapter
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> StaggeredGridLayoutManager(
                        columnCount,
                        StaggeredGridLayoutManager.VERTICAL
                    )
                }
                mAdapter =
                    WallhavenListAdapter(
                        R.layout.item_home_list,
                        activity as Context
                    ).apply {
                        setOnLoadMoreListener({
                            KLog.i(TAG, "loadmore")
                            viewModel.nextPage()
                        }, this@with)
                    }
                adapter = mAdapter
            }
        }
        return getLoadingHolder()?.getWrapper()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.search(WallHavenSearchOptions(DisplayType.TOPLIST).toQueryMap(), 1)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onDetach() {
        super.onDetach()
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            WallhavenListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}