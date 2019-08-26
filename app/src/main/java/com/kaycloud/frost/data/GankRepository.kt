package com.kaycloud.frost.data

import androidx.lifecycle.LiveData
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.AppExecutors
import com.kaycloud.frost.api.GANK_BASE_URL
import com.kaycloud.frost.api.GankService
import com.kaycloud.frost.network.NetworkBoundResource
import com.kaycloud.frost.network.converter.GankConverterFactory
import com.kaycloud.frost.network.adapter.LiveDataCallAdapterFactory
import com.kaycloud.frost.network.Resource
import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Created by kaycloud on 2019-07-16
 */
class GankRepository private constructor(
    private val gankDao: GankDao
) {

    private val okHttpClient by lazy {
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }).build()
    }

    private val gankService =
        Retrofit.Builder().baseUrl(GANK_BASE_URL).client(okHttpClient)
            .addConverterFactory(GankConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory()).build()
            .create(GankService::class.java)

    companion object {

        @Volatile
        private var instance: GankRepository? = null

        fun getInstance(gankDao: GankDao) = instance ?: synchronized(this) {
            instance ?: GankRepository(gankDao).also { instance = it }
        }
    }

    fun getGankData(page: Int): LiveData<Resource<List<GankItem>>> {
        Logger.t(TAG).d("getGankData,page:$page")
        return object :
            NetworkBoundResource<List<GankItem>, List<GankItem>>(AppExecutors.getInstance()) {
            override fun saveCallResult(item: List<GankItem>) {
                Logger.t(TAG).d(item)
                gankDao.insertAll(item)
            }

            override fun shouldFetch(data: List<GankItem>?): Boolean {
                Logger.t(TAG).d("shouldFetch")
//                return data == null || data.isEmpty()
                return true
            }

            override fun loadFromDb(): LiveData<List<GankItem>> {
                return gankDao.getAll()
            }

            override fun createCall() = gankService.getWelfare(page)


        }.asLiveData()
    }
}