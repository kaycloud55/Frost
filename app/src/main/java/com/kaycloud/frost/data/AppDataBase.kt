package com.kaycloud.frost.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.kaycloud.frost.DATABASE_NAME
import com.kaycloud.frost.workers.GankDatabaseWorker

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

    abstract fun GankDataDao(): GankDao

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
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<GankDatabaseWorker>().build()
                        WorkManager.getInstance().enqueue(request)
                    }
                }).build()
        }
    }
}
