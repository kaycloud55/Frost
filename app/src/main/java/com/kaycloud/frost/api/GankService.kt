package com.kaycloud.frost.api

import androidx.lifecycle.LiveData
import com.kaycloud.frost.data.GankItem
import com.kaycloud.frost.data.GankResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kaycloud on 2019-07-08
 */
interface GankService {

    @GET("data/福利/20/{page}")
    fun getWelfare(
        @Path("page") page: Int
    ): LiveData<ApiResponse<List<GankItem>>>
}