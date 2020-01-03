package com.kaycloud.frost.module.toplist.ui.toplist

import com.google.gson.annotations.SerializedName

/**
 * author: jiangyunkai
 * Created_at: 2020-01-03
 * 今日热榜分类数据
 * https://github.com/tophubs/TopList
 */
data class TopListCategory(val id: String, val sort: String, val title: String)

/**
 * 具体的条目信息
 */
data class ToplistItem(
    val CreateTime: Int,
    val Title: String,
    val Desc: String,
    val TypeName: String,
    val Url: String,
    val approvalNum: Int,
    val commentNum: Int,
    val id: Int,
    val imgUrl: String,
    val isRss: String,
    val is_agree: Int,
    val hotDesc: String
)


/**
 * 响应结构体
 */
data class TopListResponse<T>(
    @SerializedName("Code") val code: Int,
    @SerializedName("Message") val message: String,
    @SerializedName("Data") val data: List<T>
)