package com.kaycloud.frost.module.toplist.ui.toplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kaycloud.frost.databinding.FragmentTopListTypeBinding

/**
 * Created by jiangyunkai on 2020/4/19
 */
class TopListTypeFragment(val info: TopListType) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTopListTypeBinding.inflate(inflater, container, false)
        return binding.root
    }
}