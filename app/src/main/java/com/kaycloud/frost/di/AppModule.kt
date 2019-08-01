package com.kaycloud.frost.di

import android.app.Application
import androidx.room.Room
import com.kaycloud.frost.api.GankService
import com.kaycloud.frost.data.AppDataBase
import com.kaycloud.frost.data.GankDao
import com.kaycloud.frost.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by kaycloud on 2019-07-17
 */

@Module(includes = [ViewModelModule::class])
class AppModule {

    @Singleton
    @Provides
    fun provideGankService(): GankService {
        return Retrofit.Builder()
            .baseUrl("http://gank.io/api/data")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
            .create(GankService::class.java)
    }

    @Singleton
    @Provides
    fun provideDb(app: Application): AppDataBase {
        return Room.databaseBuilder(app.applicationContext, AppDataBase::class.java, "gank_data.db")
            .fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideGankDao(db: AppDataBase): GankDao {
        return db.GankDataDao()
    }

}