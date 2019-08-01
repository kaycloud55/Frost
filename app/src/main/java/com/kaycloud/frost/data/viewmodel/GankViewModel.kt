package com.kaycloud.frost.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kaycloud.frost.data.GankItem
import com.kaycloud.frost.data.GankRepository
import javax.inject.Inject

/**
 * Created by kaycloud on 2019-07-17
 */
class GankViewModel @Inject constructor(private val gankRepository: GankRepository) : ViewModel() {

    private val welfares: MutableLiveData<List<GankItem>> = MutableLiveData()

    fun getWelfares(): LiveData<List<GankItem>> {
        return welfares
    }

    private fun loadWelfares(page: Int) {
        gankRepository.getGankData(page)
    }
}