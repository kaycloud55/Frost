package com.kaycloud.frost.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.ListenableWorker
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.kaycloud.framework.ext.TAG
import com.kaycloud.frost.AppExecutors
import com.kaycloud.frost.DATABASE_NAME
import com.kaycloud.frost.GANK_DATA_FILENAME
import com.orhanobut.logger.Logger
import java.lang.Exception

/**
 * Created by kaycloud on 2019-07-16
 *
 * 标有 @Database注解的类需要具备以下特征：
 *      1. 继承RoomDatabase的抽象类
 *      2. 在注释中包括与数据库关联的实体列表（@Database(entities ={ })）
 *      3. 包含一个无参的抽象方法并返回一个使用@Dao注解的类
 */
@Database(entities = [GankItem::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun gankDao(): GankDao

    companion object {

        @Volatile
        private var instance: AppDataBase? = null

        fun getInstance(context: Context): AppDataBase {
            return instance ?: synchronized(this) {
                instance ?: buildDataBase(context).also { instance = it }
            }
        }

        // 创建并且预填充数据库
        private fun buildDataBase(context: Context): AppDataBase {
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        AppExecutors.getInstance().getDiskIO().execute {
                            try {
                                context.assets.open(GANK_DATA_FILENAME).use { inputSteam ->
                                    JsonReader(inputSteam.reader()).use { jsonReader: JsonReader ->
                                        val gankType = object : TypeToken<List<GankItem>>() {}.type
                                        val gankList: List<GankItem> = Gson().fromJson(jsonReader, gankType)
                                        val database = AppDataBase.getInstance(context)
                                        database.gankDao().insertAll(gankList)
                                        ListenableWorker.Result.success()
                                    }
                                }
                            } catch (ex: Exception) {
                                Logger.t(TAG).e("Error seeding databse", ex)
                                ListenableWorker.Result.failure()
                            }
                        }
                    }
                }).build()
        }
    }
}
