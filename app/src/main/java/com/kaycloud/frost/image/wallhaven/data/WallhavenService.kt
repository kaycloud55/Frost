package com.kaycloud.frost.image.wallhaven.data

import androidx.lifecycle.LiveData
import com.kaycloud.frost.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * author: kaycloud
 * Created_at: 2019-11-07
 */
interface WallhavenService {

    @GET("search")
    fun search(
        @QueryMap searchOptions: Map<String, String>? = null, @Query("page") page: Int? = null
    ): LiveData<ApiResponse<List<WallhavenItemEntity>>>

    @GET("/w/{id}")
    fun detail(@Path("id") id: String): LiveData<ApiResponse<WallhavenItemEntity>>
}