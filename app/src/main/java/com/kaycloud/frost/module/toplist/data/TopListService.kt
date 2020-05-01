package com.kaycloud.frost.module.toplist.data

import androidx.lifecycle.LiveData
import com.kaycloud.frost.module.toplist.ui.toplist.TopListItem
import com.kaycloud.frost.module.toplist.ui.toplist.CommonTopListResponse
import com.kaycloud.frost.module.toplist.ui.toplist.TopListType
import com.kaycloud.frost.module.toplist.ui.toplist.TopListTypeResponse
import com.kaycloud.frost.network.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Created by jiangyunkai on 2020/4/19
 */
interface TopListService {

    /**
     * 获取热榜各个来源
     */
    @GET("GetAllType")
    fun getAllType(): LiveData<ApiResponse<CommonTopListResponse<TopListTypeResponse>>>

    /**
     * 获取某个来源的具体内容
     */
    @GET("v2/GetAllInfoGzip?id={id}&{page}")
    fun getAllInfoByType(@Path("id") id: String, @Path("page") page: Int): LiveData<ApiResponse<List<TopListItem>>>
}