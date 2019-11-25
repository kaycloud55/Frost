package com.kaycloud.frost.module.image.gank.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.kaycloud.frost.DatabaseConstant
import com.kaycloud.frost.data.RoomListToStringConverters

/**
 * Created by kaycloud on 2019-07-16
 * 默认情况下，Room为实体中定义的每个字段创建一个列。如果有不想持久的字段，可以使用@Ignore来注解。
 * 默认情况下，Room使用类名作为数据库表名。如果希望表具有不同的名称，可以设置@Entity注解的tableName属性。（表名不区分大小写）
 * 默认情况下，Room使用字段名称作为数据库中的列名。如果希望列具有不同的名称，可以使用@ColumnInfo注解字段。
 */
@Entity(tableName = DatabaseConstant.TABLE_GANK)
@TypeConverters(RoomListToStringConverters::class)
data class GankItemEntity(
    @ColumnInfo(name = "id")
    @PrimaryKey val _id: String,
    @ColumnInfo(name = "created_at")
    val createdAt: String?,
    @ColumnInfo(name = "published_at")
    val publishedAt: String?,
    val source: String?,
    val used: String?,
    val type: String?,
    val url: String?,
    val desc: String?,
    val who: String?,
    val images: List<String>? = null
)

