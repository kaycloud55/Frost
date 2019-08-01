package com.kaycloud.frost.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kaycloud.frost.data.viewmodel.GankViewModel
import com.kaycloud.frost.data.viewmodel.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by kaycloud on 2019-07-17
 * @Binds 注解用来替代@provides注解，用来标注抽象方法，对应的Module也要变成抽象类
 * internal表示在本模块内可见
 */

@Module
abstract class ViewModelModule {

    @ViewModelKey(GankViewModel::class)
    @Binds
    @IntoMap
    internal abstract fun bindGankViewModel(gankViewModel: GankViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}