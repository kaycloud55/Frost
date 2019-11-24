package com.kaycloud.frost.image.wallhaven.ui

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
import com.kaycloud.frost.image.wallhaven.data.DisplayType
import com.kaycloud.frost.image.wallhaven.data.WallHavenSearchOptions
import com.kaycloud.frost.image.wallhaven.data.WallhavenViewModel
import com.kaycloud.frost.image.wallhaven.data.WallhavenViewModelFactory
import com.kaycloud.frost.network.Status
import com.kaycloud.frost.base.OnFragmentInteractionListener

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */

class WallhavenListFragment : Fragment() {

    private val TAG = "WallhavenListFragment"

    private var columnCount = 1

    private var listener: OnFragmentInteractionListener? = null
    private lateinit var viewModel: WallhavenViewModel

    private var mAdapter: WallhavenListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_wallhavenitementity_list, container, false)

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
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            WallhavenViewModelFactory(activity!!.application)
        )[WallhavenViewModel::class.java]
        viewModel.getSearchResult().observe(this, Observer {
            if (it.status == Status.SUCCESS && it.data != null) {
                mAdapter?.addData(it.data)
                mAdapter?.loadMoreComplete()
//                Logger.t(TAG).i("%s", it.data)
            }
        })

        viewModel.search(WallHavenSearchOptions(DisplayType.TOPLIST).toQueryMap(), 1)
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
            WallhavenListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}