package com.kaycloud.frost.module.toplist.ui.toplist

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kaycloud.frost.DatabaseConstant

/**
 * author: jiangyunkai
 * Created_at: 2020-01-03
 * 今日热榜分类数据
 * https://github.com/tophubs/TopList
 */
@Entity(tableName = DatabaseConstant.TABLE_TOP_LIST)
data class TopListType(
    @PrimaryKey val id: String,
    val icon: String,
    val name: String,
    val img: String,
    val type: String
)

/**
 * 具体的条目信息
 */

@Entity(
    foreignKeys = [ForeignKey(
        entity = TopListType::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("typeId"),
        onDelete = ForeignKey.CASCADE //删除来源时一并删除该类型下所有的数据
    )]
)
data class TopListItem(
    var typeId: String,
    val CreateTime: Int,
    val Title: String,
    val Desc: String,
    val TypeName: String,
    val Url: String,
    val approvalNum: Int,
    val commentNum: Int,
    @PrimaryKey val id: Int,
    val imgUrl: String,
    val isRss: String,
    val is_agree: Int,
    val hotDesc: String
)


/**
 * 响应结构体
 */
data class CommonTopListResponse<T>(
    val Code: Int,
    val Data: T,
    val Message: String
)

data class TopListTypeResponse(val map: Map<String, List<TopListType>>)
data class TopListItemResponse(val data: List<TopListItem>, val page: Int)
