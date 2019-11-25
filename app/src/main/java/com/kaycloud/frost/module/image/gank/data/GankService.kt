package com.kaycloud.frost.module.image.gank.data

import androidx.lifecycle.LiveData
import com.kaycloud.frost.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by kaycloud on 2019-07-08
 * http://gank.io/api/data/福利/10/1
 */
interface GankService {

    @GET("data/福利/20/{page}")
    fun getWelfare(
        @Path("page") page: Int
    ): LiveData<ApiResponse<List<GankItemEntity>>>
}