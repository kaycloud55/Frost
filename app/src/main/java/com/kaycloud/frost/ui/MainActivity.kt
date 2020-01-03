package com.kaycloud.frost.ui

import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.R
import com.kaycloud.frost.base.BaseActivity
import com.kaycloud.frost.base.OnFragmentInteractionListener
import com.kaycloud.frost.module.image.wallhaven.data.WallhavenItemEntity
import com.kaycloud.frost.module.image.gank.ui.GankListFragment
import com.kaycloud.frost.module.image.wallhaven.ui.WallhavenListFragment
import com.kaycloud.frost.module.toplist.TopListActivity
import com.kaycloud.frost.module.toplist.ui.toplist.TopListFragment
import com.orhanobut.logger.Logger

class MainActivity : BaseActivity(), OnFragmentInteractionListener {

    override fun onFragmentInteraction(item: WallhavenItemEntity) {
    }

    private var ahBottomNavigation: AHBottomNavigation? = null
    private var toolbar: Toolbar? = null

    private val gankListFragment = GankListFragment.newInstance(1)
    private val wallhavenListFragment = WallhavenListFragment.newInstance(1)
    private val topListFragment = TopListFragment.newInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        ahBottomNavigation = findViewById(R.id.bottom_navigation)
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)
        navigationAdapter.setupWithBottomNavigation(ahBottomNavigation)
        ahBottomNavigation?.setOnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> replaceFragment(gankListFragment)
                1 -> replaceFragment(wallhavenListFragment)
                2 -> replaceFragment(topListFragment)
                else -> {
                }
            }
            return@setOnTabSelectedListener true
        }
        ahBottomNavigation?.currentItem = 0


        Logger.t(TAG).i("getFilesDir():$filesDir")
        Logger.t(TAG).i("getFileslist():${fileList()}")
        Logger.t(TAG).i("getcacheDir():${cacheDir}")

        Logger.t(TAG)
            .i("getExternalStorageDir():${getExternalFilesDir(Environment.DIRECTORY_DCIM)}")

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            if (it.itemId == R.id.action_settings) {
                startActivity(Intent(this, TopListActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
