package com.kaycloud.frost.ui

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.kaycloud.frost.R
import com.kaycloud.frost.base.BaseActivity
import com.kaycloud.frost.databinding.ActivityMainBinding
import com.kaycloud.frost.util.contentView
import com.kaycloud.frost.util.hide
import com.kaycloud.frost.util.show

class MainActivity : BaseActivity() {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.apply {
            val navController = Navigation.findNavController(this@MainActivity, R.id.nav_host)
            bottomNav.setupWithNavController(navController)

            lifecycleScope.launchWhenResumed {
                navController.addOnDestinationChangedListener { _, destination, _ ->
                    when (destination.id) {
                        R.id.gank, R.id.wallhaven, R.id.toplist -> bottomNav.show()
                        else -> bottomNav.hide()
                    }
                }
            }
        }
    }
}
