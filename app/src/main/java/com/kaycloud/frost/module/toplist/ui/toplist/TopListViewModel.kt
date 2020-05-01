package com.kaycloud.frost.module.toplist.ui.toplist

import androidx.lifecycle.*
import com.kaycloud.frost.FrostApplication
import com.kaycloud.frost.data.AppDataBase

/**
 * TopList今日热榜数据
 */
class TopListViewModel : ViewModel() {

    private val _topListCategories = MediatorLiveData<List<TopListType>>()
    private val _topListItems = MediatorLiveData<List<TopListItem>>()

    val topListCategories: LiveData<List<TopListType>> = _topListCategories
    val topListItems: LiveData<List<TopListItem>> = _topListItems


    fun getTopListTypes() {
        _topListCategories.addSource(
            TopListRepository.getInstance(AppDataBase.getInstance(FrostApplication.applicationContext()).topListDao())
                .getTopListCategory()
        ) {
            _topListCategories.value = it.data
        }
    }
}


