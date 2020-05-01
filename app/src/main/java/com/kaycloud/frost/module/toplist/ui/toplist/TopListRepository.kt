package com.kaycloud.frost.module.toplist.ui.toplist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kaycloud.framework.log.KLog
import com.kaycloud.frost.api.TOP_LIST_URL
import com.kaycloud.frost.module.toplist.data.TopListDao
import com.kaycloud.frost.module.toplist.data.TopListService
import com.kaycloud.frost.network.*

/**
 * author: jiangyunkai
 * Created_at: 2020-01-03
 * 今日热榜数据请求
 */

private const val TAG = "TopListRepository"

class TopListRepository private constructor(private val topListDao: TopListDao) {

    val topListType: MutableLiveData<List<TopListType>> = MutableLiveData()
    val topListItems: MutableLiveData<List<TopListItem>> = MutableLiveData()

    private val topListService by lazy {
        NetworkRequester.getRetrofitClient(TOP_LIST_URL).create(TopListService::class.java)
    }

    companion object {

        @Volatile
        private var instance: TopListRepository? = null

        fun getInstance(topListDao: TopListDao): TopListRepository =
            instance ?: synchronized(this) {
                instance ?: TopListRepository(topListDao).also {
                    instance = it
                }
            }
    }

    fun getTopListCategory(): LiveData<Resource<List<TopListType>>> {
        KLog.i(TAG, "getTopListCategory")

        return object :
            NetworkBoundResource<List<TopListType>, CommonTopListResponse<TopListTypeResponse>>() {
            override fun saveCallResult(item: CommonTopListResponse<TopListTypeResponse>) {
                topListDao.insertAllTypes(item.Data.map)
            }

            override fun shouldFetch(data: List<TopListType>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<TopListType>> {
                return topListDao.loadAllTypes()
            }

            override fun createCall(): LiveData<ApiResponse<CommonTopListResponse<TopListType>>> =
                topListService.getAllType()

        }.asLiveData()

    }

    fun getTopListItems(id: String, page: Int): LiveData<Resource<List<TopListItem>>> {
        KLog.i(TAG, "getTopListItems")

        return object : NetworkBoundResource<List<TopListItem>, List<TopListItem>>() {
            override fun saveCallResult(item: List<TopListItem>) {
                topListDao.insertAllItems(item)
            }

            override fun shouldFetch(data: List<TopListItem>?): Boolean {
                return true
            }

            override fun loadFromDb(): LiveData<List<TopListItem>> {
                return topListDao.loadItemsByType(id, 20, page)
            }

            override fun createCall(): LiveData<ApiResponse<List<TopListItem>>> =
                topListService.getAllInfoByType(id, page)

            override fun processResponse(response: ApiSuccessResponse<List<TopListItem>>): List<TopListItem> {
                val result = response.body
                result.forEach {
                    it.typeId = id
                }
                return result
            }

        }.asLiveData()
    }
}