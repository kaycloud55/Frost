package com.kaycloud.frost.module.toplist.ui.toplist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.kaycloud.framework.ext.TAG
import com.kaycloud.framework.log.KLog
import com.kaycloud.frost.databinding.TopListFragmentBinding

class TopListFragment : Fragment() {

    private var binding: TopListFragmentBinding? = null

    private val viewModel: TopListViewModel by lazy {
        ViewModelProvider(requireActivity()).get(TopListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = TopListFragmentBinding.inflate(inflater, container, false).apply {
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.topListCategories.observeForever {
            if (it != null) {
                KLog.i(TAG, "result", it)
                binding?.topListViewPager?.adapter = TopListTypePagerAdapter(this, it)
            }
        }
        viewModel.getTopListTypes()
    }


}
