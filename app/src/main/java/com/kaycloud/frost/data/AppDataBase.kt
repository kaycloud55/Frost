package com.kaycloud.frost.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.kaycloud.frost.DATABASE_NAME
import com.kaycloud.frost.module.image.gank.data.GankDao
import com.kaycloud.frost.module.image.wallhaven.data.db.WallhavenDao
import com.kaycloud.frost.module.image.gank.data.GankItemEntity
import com.kaycloud.frost.module.image.wallhaven.data.WallhavenItemEntity

/**
 * Created by kaycloud on 2019-07-16
 *
 * 标有 @Database注解的类需要具备以下特征：
 *      1. 继承RoomDatabase的抽象类
 *      2. 在注释中包括与数据库关联的实体列表（@Database(entities ={ })）
 *      3. 包含一个无参的抽象方法并返回一个使用@Dao注解的类
 *
 * Room不允许在主线程中访问数据库，除非在build的时候使用allowMainThreadQueries()方法
 */
@Database(
    entities = [GankItemEntity::class, WallhavenItemEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun gankDao(): GankDao

    abstract fun wallhavenDao(): WallhavenDao

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
            //context,
            return Room.databaseBuilder(context, AppDataBase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    //重写`onCreate()`方法，在创建数据库的时候填充数据
                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        AppExecutors.getInstance().getDiskIO().execute {
//                            try {
//                                context.assets.open(GANK_DATA_FILENAME).use { inputSteam ->
//                                    JsonReader(inputSteam.reader()).use { jsonReader: JsonReader ->
//                                        val gankType =
//                                            object : TypeToken<List<GankItemEntity>>() {}.type
//                                        val gankListEntity: List<GankItemEntity> =
//                                            Gson().fromJson(jsonReader, gankType)
//                                        val database = getInstance(context)
//                                        database.gankDao().insertAll(gankListEntity)
//                                        ListenableWorker.Result.success()
//                                        Logger.t(TAG).i("预填充数据库成功")
//                                    }
//                                }
//                            } catch (ex: Exception) {
//                                Logger.t(TAG).e("Error seeding databse", ex)
//                                ListenableWorker.Result.failure()
//                            }
//                        }
                    }
                }).build()
        }
    }
}
