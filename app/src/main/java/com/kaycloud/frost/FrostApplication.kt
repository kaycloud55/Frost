package com.kaycloud.frost

import android.app.Application
import android.content.Context
import com.facebook.stetho.Stetho
import com.kaycloud.framework.AppGlobals
import com.kaycloud.framework.image.imageloader.GlideImageLoadStrategy
import com.kaycloud.framework.image.imageloader.ImageLoader
import com.kaycloud.frost.base.loading.DefaultLoadAdapter
import com.kaycloud.frost.base.loading.LoadingManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure

/**
 * Created by kaycloud on 2019-07-17
 */
class FrostApplication : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: FrostApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }


    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AppGlobals.sApplicationContext = base
    }

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        Stetho.initializeWithDefaults(this)
        CrashReport.initCrashReport(applicationContext, BUGLY_APPID, true)
        UMConfigure.init(this, UMENG_APPKEY, "official", UMConfigure.DEVICE_TYPE_PHONE, "")
        UMConfigure.setLogEnabled(true)

        LoadingManager.initDefault(DefaultLoadAdapter())

        //init Glide
        ImageLoader.setLoadStrategy(GlideImageLoadStrategy())
    }
}