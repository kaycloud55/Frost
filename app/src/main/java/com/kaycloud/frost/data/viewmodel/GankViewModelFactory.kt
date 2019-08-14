package com.kaycloud.frost.data.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.RuntimeException
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

/**
 * Created by kaycloud on 2019-07-17
 * 为什么要使用ViewModelFactory?
 * 为了填充参数？
 */
class GankViewModelFactory internal constructor(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GankViewModel(application) as T
    }

}