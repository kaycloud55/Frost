package com.kaycloud.frost

import android.app.Application
import com.facebook.stetho.Stetho
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger

/**
 * Created by kaycloud on 2019-07-17
 */
class FrostApp : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.addLogAdapter(AndroidLogAdapter())
        Stetho.initializeWithDefaults(this)
    }
}