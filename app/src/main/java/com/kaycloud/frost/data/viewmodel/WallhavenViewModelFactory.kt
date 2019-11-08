package com.kaycloud.frost.data.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Created by kaycloud on 2019-07-17
 * 为什么要使用ViewModelFactory?
 * 为了填充参数？
 */
class WallhavenViewModelFactory internal constructor(private val application: Application) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WallhavenViewModel(application) as T
    }
}