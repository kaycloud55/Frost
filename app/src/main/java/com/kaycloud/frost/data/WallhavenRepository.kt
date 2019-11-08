package com.kaycloud.frost.data

import androidx.lifecycle.LiveData
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.AppExecutors
import com.kaycloud.frost.api.WALL_HAVEN_URL
import com.kaycloud.frost.api.WallhavenService
import com.kaycloud.frost.data.dao.WallhavenDao
import com.kaycloud.frost.data.entity.WallhavenItemEntity
import com.kaycloud.frost.network.NetworkBoundResource
import com.kaycloud.frost.network.NetworkRequester
import com.kaycloud.frost.network.Resource
import com.orhanobut.logger.Logger

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 */
class WallhavenRepository private constructor(private val wallhavenDao: WallhavenDao) {

    private val mWallhavenService = NetworkRequester.getRequestClient(WALL_HAVEN_URL).create(
        WallhavenService::class.java
    )

    companion object {

        @Volatile
        private var instance: WallhavenRepository? = null

        fun getInstance(wallhavenDao: WallhavenDao) = instance ?: synchronized(this) {
            instance ?: WallhavenRepository(wallhavenDao).also { instance = it }
        }
    }

    fun getWallhavenData(searchOptions: Map<String, String>):
            LiveData<Resource<List<WallhavenItemEntity>>> {
        Logger.t(TAG).d("getGankData,page:$searchOptions")
        return object :
            NetworkBoundResource<List<WallhavenItemEntity>, List<WallhavenItemEntity>>(
                AppExecutors.getInstance()
            ) {
            override fun saveCallResult(itemEntity: List<WallhavenItemEntity>) {
                Logger.t(TAG).d(itemEntity)
                wallhavenDao.insertAll(itemEntity)
            }

            override fun shouldFetch(data: List<WallhavenItemEntity>?): Boolean {
                Logger.t(TAG).d("shouldFetch")
                return true
            }

            override fun loadFromDb(): LiveData<List<WallhavenItemEntity>> {
                return wallhavenDao.getAll()
            }

            override fun createCall() = mWallhavenService.search(searchOptions)
        }.asLiveData()
    }
}