package com.kaycloud.frost.di

import com.kaycloud.frost.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by kaycloud on 2019-07-17
 */
@Module
abstract class MainActivityModule {

    //这个注解表明，需要把FragmentBuilderModule注入到MainActivity中
    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeMainActivity(): MainActivity
}