package com.kaycloud.frost.image.gank.data

import androidx.lifecycle.LiveData
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.AppExecutors
import com.kaycloud.frost.api.GANK_BASE_URL
import com.kaycloud.frost.network.NetworkBoundResource
import com.kaycloud.frost.network.NetworkRequester
import com.kaycloud.frost.network.Resource
import com.orhanobut.logger.Logger

/**
 * Created by kaycloud on 2019-07-16
 */
class GankRepository private constructor(
    private val gankDao: GankDao
) {

    private val mGankService =
        NetworkRequester.getRequestClient(GANK_BASE_URL).create(GankService::class.java)

    companion object {

        @Volatile
        private var instance: GankRepository? = null

        fun getInstance(gankDao: GankDao) = instance
            ?: synchronized(this) {
                instance
                    ?: GankRepository(gankDao).also { instance = it }
            }
    }

    fun getGankData(page: Int): LiveData<Resource<List<GankItemEntity>>> {
        Logger.t(TAG).d("getGankData,page:$page")
        return object :
            NetworkBoundResource<List<GankItemEntity>, List<GankItemEntity>>(
                AppExecutors.getInstance()
            ) {
            override fun saveCallResult(itemEntity: List<GankItemEntity>) {
                Logger.t(TAG).d("%s", itemEntity)
                gankDao.insertAll(itemEntity)
            }

            override fun shouldFetch(data: List<GankItemEntity>?): Boolean {
                Logger.t(TAG).d("shouldFetch")
                return true
            }

            override fun loadFromDb(): LiveData<List<GankItemEntity>> {
                return gankDao.loadAllByPages(20, (page - 1) * 20)
            }

            override fun createCall() = mGankService.getWelfare(page)
        }.asLiveData()
    }
}