package com.kaycloud.frost.ui

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.R
import com.kaycloud.frost.base.OnFragmentInteractionListener
import com.kaycloud.frost.image.wallhaven.data.WallhavenItemEntity
import com.kaycloud.frost.image.gank.ui.GankListFragment
import com.kaycloud.frost.image.wallhaven.ui.WallhavenListFragment
import com.orhanobut.logger.Logger

class MainActivity : AppCompatActivity(), OnFragmentInteractionListener {

    override fun onFragmentInteraction(item: WallhavenItemEntity) {
    }

    private var ahBottomNavigation: AHBottomNavigation? = null

    private val gankListFragment = GankListFragment.newInstance(2)
    private val wallhavenListFragment = WallhavenListFragment.newInstance(2)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        ahBottomNavigation = findViewById(R.id.bottom_navigation)
        val navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation)
        navigationAdapter.setupWithBottomNavigation(ahBottomNavigation)
        ahBottomNavigation?.setOnTabSelectedListener { position, wasSelected ->
            when (position) {
                0 -> replaceFragment(gankListFragment)
                1 -> replaceFragment(wallhavenListFragment)
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
}
