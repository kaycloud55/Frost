package com.kaycloud.frost.data

import androidx.lifecycle.LiveData
import com.kaycloud.frost.AppExecutors
import com.kaycloud.frost.api.GankService
import com.kaycloud.frost.vo.Resource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by kaycloud on 2019-07-16
 */
@Singleton
class GankRepository @Inject constructor(
    private val appExecutors: AppExecutors,
    private val gankDao: GankDao,
    private val db: AppDataBase,
    private val gankService: GankService
) {


//    fun getGankDatas() = gankDao.getAll()
//
//    fun getGankItem(id: String) = gankDao.getGankItem(id)

//    companion object {
//
//        @Volatile
//        private var instance: GankRepository? = null
//
//        fun getInstance(gankDao: GankDao) = instance ?: synchronized(this) {
//            instance ?: GankRepository(gankDao).also { instance = it }
//        }
//    }

    fun getGankData(page: Int): LiveData<Resource<List<GankItem>>> {

        return object : NetworkBoundResource<List<GankItem>, List<GankItem>>(appExecutors) {
            override fun saveCallResult(item: List<GankItem>) {
                gankDao.insertAll(item)
            }

            override fun shouldFetch(data: List<GankItem>?): Boolean {
                return data == null
            }

            override fun loadFromDb(): LiveData<List<GankItem>> {
                return gankDao.getAll()
            }

            override fun createCall() = gankService.getWelfare(page)


        }.asLiveData()

    }
}