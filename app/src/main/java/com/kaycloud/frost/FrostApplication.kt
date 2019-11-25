package com.kaycloud.frost

import android.app.Application
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.tencent.bugly.crashreport.CrashReport
import com.umeng.commonsdk.UMConfigure

/**
 * Created by kaycloud on 2019-07-17
 */
class FrostApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        Stetho.initializeWithDefaults(this)
        CrashReport.initCrashReport(applicationContext, BUGLY_APPID, true)
        UMConfigure.init(this, UMENG_APPKEY, "official", UMConfigure.DEVICE_TYPE_PHONE, "")
        UMConfigure.setLogEnabled(true)
    }
}