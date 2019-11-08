package com.kaycloud.frost.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kaycloud.frost.R
import com.kaycloud.frost.data.entity.WallhavenItemEntity

class MainActivity : AppCompatActivity(), GankListFragment.OnListFragmentInteractionListener,
    WallhavenListFragment.OnListFragmentInteractionListener {

    override fun onListFragmentInteraction(item: WallhavenItemEntity) {

    }

    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView?.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_girl -> {
                    replaceFragment(GankListFragment.newInstance(1))
                }
                R.id.item_news -> {
                    replaceFragment(WallhavenListFragment.newInstance(1))
                }
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_content, fragment)
        fragmentTransaction.commit()
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
