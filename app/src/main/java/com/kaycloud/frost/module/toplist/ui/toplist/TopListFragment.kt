package com.kaycloud.frost.module.toplist.ui.toplist

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaycloud.frost.R
import com.kaycloud.frost.module.toplist.TopListActivity

class TopListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var mAdapter: TopListCategoryAdapter? = null

    companion object {
        fun newInstance() = TopListFragment()
    }

    private lateinit var viewModel: TopListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.top_list_fragment, container, false)
        recyclerView = view.findViewById(R.id.rl_top_list)
        with(recyclerView!!) {
            adapter =
                TopListCategoryAdapter(R.layout.item_top_list_category, activity as Context).also {
                    mAdapter = it
                    it.setOnItemClickListener { adapter, view, position ->
                        context.startActivity(Intent(context, TopListActivity::class.java).apply {
                            putExtra("id", (adapter.data[position] as TopListCategory).id)
                        })
                    }
                }
            layoutManager = LinearLayoutManager(
                this@TopListFragment.context,
                LinearLayoutManager.VERTICAL,
                false
            )
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(TopListViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.toplistCategories.observe(this, Observer {
            mAdapter?.addData(it)
            mAdapter?.notifyDataSetChanged()
        })
        viewModel.getTopListCategory()
    }

}
