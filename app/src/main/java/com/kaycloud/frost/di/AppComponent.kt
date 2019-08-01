package com.kaycloud.frost.di

import android.app.Application
import com.kaycloud.frost.FrostApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by kaycloud on 2019-07-17
 * 对于dagger-Android来说，只需要一个component就足够了
 * 使用Builder模式构建Component，方便灵活的向Component中添加构建属性
 */
@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class, MainActivityModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(frostApp: FrostApp)
}