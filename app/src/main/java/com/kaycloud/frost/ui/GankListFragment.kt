package com.kaycloud.frost.ui

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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.R
import com.kaycloud.frost.data.entity.GankItemEntity
import com.kaycloud.frost.data.entity.WallhavenItemEntity
import com.kaycloud.frost.data.viewmodel.GankViewModel
import com.kaycloud.frost.data.viewmodel.GankViewModelFactory
import com.kaycloud.frost.network.Status
import com.kaycloud.frost.ui.adapter.GankListAdapter
import com.orhanobut.logger.Logger

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */
class GankListFragment : Fragment() {

    private var columnCount = 1
    private var listener: OnListFragmentInteractionListener? = null
    private val mItemList: MutableList<GankItemEntity> = mutableListOf()

    private var mAdapter: GankListAdapter? = null

    private lateinit var viewModel: GankViewModel

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
                    else -> GridLayoutManager(context, columnCount)
                }
                mAdapter =
                    GankListAdapter(
                        R.layout.item_home_list,
                        mItemList,
                        activity as Context
                    )
                adapter = mAdapter
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(
            this,
            GankViewModelFactory(activity!!.application)
        )[GankViewModel::class.java]
        viewModel.getWelfare().observe(this, Observer {
            if (it.status == Status.SUCCESS && it.data != null) {
                mItemList.clear()
                mItemList.addAll(it.data)
                Logger.t(TAG).i("", mItemList)
                mAdapter?.notifyDataSetChanged()
            }
        })

        viewModel.setPage(0)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnListFragmentInteractionListener) {
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson
     * [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: WallhavenItemEntity)
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