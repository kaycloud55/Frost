package com.kaycloud.frost.module.toplist.ui.toplist

import android.view.animation.Transformation
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

/**
 * TopList今日热榜数据
 */
class TopListViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    var toplistCategories: MediatorLiveData<List<TopListCategory>> = MediatorLiveData()

    fun getTopListCategory() {
        toplistCategories.removeSource(TopListRepository.getInstance().topListCategory)
        toplistCategories.addSource(TopListRepository.getInstance().topListCategory) {
            toplistCategories.value = it
        }
        TopListRepository.getInstance().getTopListCategory()
    }

}


