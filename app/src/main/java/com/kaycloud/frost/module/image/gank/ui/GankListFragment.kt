package com.kaycloud.frost.module.image.gank.ui

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kaycloud.framework.log.KLog
import com.kaycloud.frost.R
import com.kaycloud.frost.base.BaseFragment
import com.kaycloud.frost.base.FrostLoadMoreView
import com.kaycloud.frost.module.image.gank.data.GankItemEntity
import com.kaycloud.frost.module.image.gank.data.GankViewModel
import com.kaycloud.frost.module.image.gank.data.GankViewModelFactory
import com.kaycloud.frost.network.Status
import com.kaycloud.frost.base.OnFragmentInteractionListener
import com.orhanobut.logger.Logger


class GankListFragment : BaseFragment() {
    private val TAG = "GankListFragment"

    private var rootView: View? = null
    private var columnCount = 1
    private var listener: OnFragmentInteractionListener? = null
    private val mItemList: MutableList<GankItemEntity> = mutableListOf()

    private var mAdapter: GankListAdapter? = null

    private lateinit var viewModel: GankViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
        viewModel = ViewModelProviders.of(this)[GankViewModel::class.java]
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
                    GankListAdapter(
                        R.layout.item_home_list,
                        mItemList,
                        activity as Context
                    ).apply {
                        setOnLoadMoreListener({
                            viewModel.nextPage()
                            KLog.i(TAG, "currentpge: ${viewModel.page.value}")
                        }, this@with)
                        setLoadMoreView(FrostLoadMoreView())
                    }
                adapter = mAdapter

            }
        }

        return getLoadingHolder()?.getWrapper()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        subscribeUi()
        viewModel.setPage(0)
    }

    override fun onLoadRetry() {

    }

    private fun subscribeUi() {
        viewModel.getWelfare().observe(this, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data != null) {
                        showContent()
                        mAdapter?.addData(it.data)
                        mAdapter?.loadMoreComplete()
                        Logger.t(TAG).i("%s", mItemList)
                    } else {
                        showEmpty()
                    }
                }
                Status.LOADING -> {
                    showLoading()
                    KLog.i(TAG, "isLoading")
                }
                Status.ERROR -> {
                    showLoadFailed()
                }

            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(
                "$context must implement OnListFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {

        const val ARG_COLUMN_COUNT = "column-count"

        @JvmStatic
        fun newInstance(columnCount: Int) =
            GankListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}