package com.kaycloud.frost.module.ui.home

import android.app.Activity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by jiangyunkai on 2020/1/8
 */
class ScreenViewPagerAdapter(context: Fragment) : FragmentStateAdapter(context) {

    override fun createFragment(position: Int): Fragment {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount() = 3

}