package com.kaycloud.frost.module.toplist

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kaycloud.frost.R
import com.kaycloud.frost.module.toplist.ui.toplist.TopListFragment
import com.kaycloud.frost.module.toplist.ui.toplist.TopListItemAdapter
import com.kaycloud.frost.module.toplist.ui.toplist.TopListViewModel

class TopListActivity : AppCompatActivity() {

    private var recyclerView: RecyclerView? = null
    private var mAdapter: TopListItemAdapter? = null

    private lateinit var viewModel: TopListViewModel
    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top_list_activity)
        id = intent.getStringExtra("id")
        recyclerView = findViewById(R.id.rl_list)
        with(recyclerView!!) {
            adapter =
                TopListItemAdapter(R.layout.item_top_list_category, this@TopListActivity).also {
                    mAdapter = it
                }
            layoutManager =
                LinearLayoutManager(this@TopListActivity, LinearLayoutManager.VERTICAL, false)
        }

    }

    override fun onStart() {
        viewModel = ViewModelProviders.of(this).get(TopListViewModel::class.java)
        // TODO: Use the ViewModel
        viewModel.toplistItems.observe(this, Observer {
            mAdapter?.setNewData(it)
            mAdapter?.notifyDataSetChanged()
        })
        viewModel.getTopListItems(id)
        return super.onStart()
    }

}
