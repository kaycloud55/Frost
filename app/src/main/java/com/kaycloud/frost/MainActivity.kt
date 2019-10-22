package com.kaycloud.frost

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.kaycloud.frost.data.GankItem
import com.kaycloud.frost.data.viewmodel.GankViewModel
import com.kaycloud.frost.data.viewmodel.GankViewModelFactory
import com.kaycloud.frost.ui.HomeAdapter
import com.orhanobut.logger.Logger

class MainActivity : AppCompatActivity() {

    private lateinit var gankViewModel: GankViewModel

    private lateinit var homeAdapter: HomeAdapter

    private var mDataList: MutableList<GankItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val recyclerView: RecyclerView = findViewById(R.id.rv_main)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        homeAdapter = HomeAdapter(R.layout.item_home_list, mDataList, this)
        homeAdapter.openLoadAnimation()
        homeAdapter.notifyDataSetChanged()
        recyclerView.adapter = homeAdapter

        initData()
    }

    private fun initData() {
        gankViewModel = ViewModelProviders.of(
            this,
            GankViewModelFactory(application)
        )[GankViewModel::class.java]
        gankViewModel.getWelfare().observe(this, Observer {
            it.data?.apply {
                mDataList.addAll(this)
                homeAdapter.notifyDataSetChanged()
            }

            Logger.t("MainActivity").d(it)
        })
        gankViewModel.setPage(0)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
