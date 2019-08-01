package com.kaycloud.frost.data

import androidx.room.*

/**
 * Created by kaycloud on 2019-07-16
 * 默认情况下，Room为实体中定义的每个字段创建一个列。如果有不想持久的字段，可以使用@Ignore来注解。
 * 默认情况下，Room使用类名作为数据库表名。如果希望表具有不同的名称，可以设置@Entity注解的tableName属性。（表名不区分大小写）
 * 默认情况下，Room使用字段名称作为数据库中的列名。如果希望列具有不同的名称，可以使用@ColumnInfo注解字段。
 */
data class GankResponse(@Ignore val error: Boolean, private val results: List<GankItem>)

@Entity(tableName = "gank_data")
data class GankItem(
    @PrimaryKey
    val id: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: String? = null,
    @ColumnInfo(name = "published_at")
    val publishedAt: String? = null,
    val source: String? = null,
    val used: String? = null,
    val type: String? = null,
    val url: String? = null,
    val desc: String? = null,
    val who: String? = null,
    val images: List<String>? = null
)