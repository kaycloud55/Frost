package com.kaycloud.frost.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.kaycloud.frost.BuildConfig
import com.kaycloud.frost.network.adapter.LiveDataCallAdapterFactory
import com.kaycloud.frost.network.converter.GankConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * author: kaycloud
 * Created_at: 2019-11-07
 */
object NetworkRequester {

    private val sOkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .addInterceptor(StethoInterceptor())
            .readTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    fun getRequestClient(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(sOkHttpClient)
            .addConverterFactory(GankConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }
}
