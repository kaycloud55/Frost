package com.kaycloud.frost.data

import androidx.lifecycle.LiveData
import com.android.example.github.repository.NetworkBoundResource
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.AppExecutors
import com.kaycloud.frost.api.GANK_BASE_URL
import com.kaycloud.frost.api.GankService
import com.kaycloud.frost.util.LiveDataCallAdapterFactory
import com.kaycloud.frost.vo.Resource
import com.orhanobut.logger.Logger
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by kaycloud on 2019-07-16
 */
class GankRepository private constructor(
    private val gankDao: GankDao
) {

    private val gankService =
        Retrofit.Builder().baseUrl(GANK_BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory()).build().create(GankService::class.java)

    companion object {

        @Volatile
        private var instance: GankRepository? = null

        fun getInstance(gankDao: GankDao) = instance ?: synchronized(this) {
            instance ?: GankRepository(gankDao).also { instance = it }
        }
    }

    fun getGankData(page: Int): LiveData<Resource<List<GankItem>>> {
        Logger.t(TAG).d("getGankData,page:$page")
        return object : NetworkBoundResource<List<GankItem>, List<GankItem>>(AppExecutors.getInstance()) {
            override fun saveCallResult(item: List<GankItem>) {
                gankDao.insertAll(item)
            }

            override fun shouldFetch(data: List<GankItem>?): Boolean {
                Logger.t(TAG).d("shouldFetch")
                return data == null
            }

            override fun loadFromDb(): LiveData<List<GankItem>> {
                Logger.t(TAG).d("loadFromDb")
                return gankDao.getAll()
            }

            override fun createCall() = gankService.getWelfare(page)


        }.asLiveData()
    }
}