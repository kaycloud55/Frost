package com.kaycloud.frost.network

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.kaycloud.frost.BuildConfig
import com.kaycloud.frost.network.adapter.LiveDataCallAdapterFactory
import com.kaycloud.frost.network.converter.GankConverterFactory
import com.orhanobut.logger.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

/**
 * author: kaycloud
 * Created_at: 2019-11-07
 */
private const val TAG = "NetworkRequester"

object NetworkRequester {

    private val sOkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
                override fun log(message: String) {
                    Logger.t(TAG).i(message)
                }
            }).apply {
                if (BuildConfig.DEBUG) {
                    level = HttpLoggingInterceptor.Level.BODY
                }
            })
            .addNetworkInterceptor(StethoInterceptor())
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .connectTimeout(10, TimeUnit.SECONDS)
            .build()

        //https 证书问题
        val trustManager: Array<TrustManager> = arrayOf(object : X509TrustManager {
            override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        })
        val ssl = SSLContext.getInstance("SSL")
        ssl.init(null, trustManager, SecureRandom())

        HttpsURLConnection.setDefaultSSLSocketFactory(ssl.socketFactory)
        HttpsURLConnection.setDefaultHostnameVerifier { hostname, session ->
            return@setDefaultHostnameVerifier true
        }
        return@lazy builder
    }

    fun getRetrofitClient(baseUrl: String): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .client(sOkHttpClient)
            .addConverterFactory(GankConverterFactory.create())
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .build()
    }

    fun getOkhttpClient() = sOkHttpClient
}
