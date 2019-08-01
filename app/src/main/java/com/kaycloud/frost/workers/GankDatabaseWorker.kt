package com.kaycloud.frost.workers

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.kaycloud.frost.GANK_DATA_FILENAME
import com.kaycloud.frost.data.AppDataBase
import com.kaycloud.frost.data.GankItem
import com.orhanobut.logger.Logger
import kotlinx.coroutines.coroutineScope
import java.lang.Exception

/**
 * Created by kaycloud on 2019-07-16
 */
class GankDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG by lazy { GankDatabaseWorker::class.java.simpleName }

    override suspend fun doWork(): Result = coroutineScope {
        try {
            applicationContext.assets.open(GANK_DATA_FILENAME).use { inputSteam ->
                JsonReader(inputSteam.reader()).use { jsonReader: JsonReader ->
                    val gankType = object : TypeToken<List<GankItem>>() {}.type
                    val gankList: List<GankItem> = Gson().fromJson(jsonReader, gankType)
                    val database = AppDataBase.getInstance(applicationContext)
                    Result.success()
                }
            }
        } catch (ex: Exception) {
            Logger.t(TAG).e("Error seeding databse", ex)
            Result.failure()
        }
    }

}