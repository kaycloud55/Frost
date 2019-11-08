package com.kaycloud.frost.data.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.kaycloud.frost.data.AppDataBase
import com.kaycloud.frost.data.WallhavenRepository
import com.kaycloud.frost.data.entity.WallhavenItemEntity
import com.kaycloud.frost.network.Resource

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */
class WallhavenViewModel internal constructor(context: Context) : ViewModel() {

    private val mWallhavenRepository = WallhavenRepository.getInstance(
        AppDataBase.getInstance
            (context).wallhavenDao()
    )

    private val _queryOptions: MutableLiveData<MutableMap<String, String>> = MutableLiveData()

    private var searchResult: LiveData<Resource<List<WallhavenItemEntity>>> = Transformations
        .switchMap(_queryOptions) { input -> mWallhavenRepository.getWallhavenData(input) }

    fun getSearchResult() = searchResult

    fun search(queryOptions: Map<String, String>) {
        _queryOptions.value = queryOptions.toMutableMap()
    }
}