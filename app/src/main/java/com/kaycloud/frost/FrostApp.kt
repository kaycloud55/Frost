package com.kaycloud.frost

import android.app.Activity
import android.app.Application
import com.kaycloud.frost.di.AppInjector
import com.orhanobut.logger.Logger
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import java.util.*
import javax.inject.Inject

/**
 * Created by kaycloud on 2019-07-17
 */
class FrostApp : Application(), HasActivityInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun activityInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }


}