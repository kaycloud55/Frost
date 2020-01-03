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
    var toplistItems: MediatorLiveData<List<ToplistItem>> = MediatorLiveData()

    fun getTopListCategory() {
        toplistCategories.removeSource(TopListRepository.getInstance().topListCategory)
        toplistCategories.addSource(TopListRepository.getInstance().topListCategory) {
            toplistCategories.value = it
        }
        TopListRepository.getInstance().getTopListCategory()
    }

    fun getTopListItems(id: String) {
        toplistItems.removeSource(TopListRepository.getInstance().topListItems)
        toplistItems.addSource(TopListRepository.getInstance().topListItems) {
            toplistItems.value = it
        }
        TopListRepository.getInstance().getTopListItems(id)
    }


}


