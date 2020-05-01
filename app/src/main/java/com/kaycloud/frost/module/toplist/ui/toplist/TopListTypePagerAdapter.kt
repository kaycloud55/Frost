package com.kaycloud.frost.module.toplist.ui.toplist

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * Created by jiangyunkai on 2020/4/19
 */
class TopListTypePagerAdapter(fragment: Fragment, private val types: List<TopListType>) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return types.size
    }

    override fun createFragment(position: Int): Fragment {
        return TopListTypeFragment(types[position])
    }
}