package com.kaycloud.frost.module.toplist.ui.toplist

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaycloud.frost.R

class TopListFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var adapter: TopListCategoryAdapter? = null

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
            adapter = TopListCategoryAdapter(R.layout.item_home_list)
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
            adapter?.setNewData(it)
            adapter?.notifyDataSetChanged()
        })
        viewModel.getTopListCategory()
    }

}
