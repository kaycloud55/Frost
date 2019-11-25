package com.kaycloud.frost.module.image.wallhaven.data

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.kaycloud.framework.ext.TAG
import com.kaycloud.framework.log.KLog
import com.kaycloud.frost.data.AppDataBase
import com.kaycloud.frost.network.Resource

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 * 传入的Context决定了它绑定哪个生命周期
 */
class WallhavenViewModel internal constructor(application: Application) :
    AndroidViewModel(application) {

    private val TAG = "WallhavenViewModel"

    private val mWallhavenRepository = WallhavenRepository.getInstance(
        AppDataBase.getInstance
            (application).wallhavenDao()
    )

    private val _queryOptions: MutableLiveData<MutableMap<String, String>> = MutableLiveData()
    private var _page: MutableLiveData<Int> = MutableLiveData()

    val page: LiveData<Int>
        get() = _page

    private var searchResultFromOptions: LiveData<Resource<List<WallhavenItemEntity>>> =
        Transformations
            .switchMap(_queryOptions) { input ->
                KLog.i(TAG, "searchResultFromOptions change")
                mWallhavenRepository.getWallhavenData(
                    input,
                    page.value
                )
            }

    private var searchResultFromPage: LiveData<Resource<List<WallhavenItemEntity>>> =
        Transformations
            .switchMap(_page) { input ->
                KLog.i(TAG, "searchResultFromPage change")
                mWallhavenRepository.getWallhavenData(
                    _queryOptions.value!!,
                    input
                )
            }

    private var liveDataManager = object : MediatorLiveData<Resource<List<WallhavenItemEntity>>>() {
        override fun onActive() {
            KLog.i(TAG, "liveDataManager onActive")
            super.onActive()
        }

        override fun onInactive() {
            KLog.i(TAG, "liveDataManager onInActive")
            super.onInactive()
        }

    }.apply {
        addSource(searchResultFromOptions) {
            this.value = it
        }
        addSource(searchResultFromPage) {
            this.value = it
        }

    }

    fun getSearchResult() = liveDataManager

    fun search(queryOptions: Map<String, String>, page: Int? = null) {
        _queryOptions.value = queryOptions.toMutableMap()
    }

    fun nextPage() {
        setPage((page.value ?: 1) + 1)
    }

    private fun setPage(newValue: Int) {
        if (this._page.value == newValue) {
            return
        }
        _page.value = newValue
    }
}