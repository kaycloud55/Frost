package com.kaycloud.frost.module.toplist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kaycloud.frost.R
import com.kaycloud.frost.module.toplist.ui.toplist.TopListFragment

class TopListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.top_list_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, TopListFragment.newInstance())
                .commitNow()
        }
    }

}
