package com.kaycloud.frost.data

import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Created by kaycloud on 2019-07-16
 * 默认情况下，Room为实体中定义的每个字段创建一个列。如果有不想持久的字段，可以使用@Ignore来注解。
 * 默认情况下，Room使用类名作为数据库表名。如果希望表具有不同的名称，可以设置@Entity注解的tableName属性。（表名不区分大小写）
 * 默认情况下，Room使用字段名称作为数据库中的列名。如果希望列具有不同的名称，可以使用@ColumnInfo注解字段。
 */
@Entity(tableName = "gank_data")
@TypeConverters(ImageConverters::class)
data class GankItem(
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

class ImageConverters {

    @TypeConverter
    fun stringToObject(value: String): List<String>? {
        val listType = object : TypeToken<List<String>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun objectToString(list: List<String>?): String {
        return Gson().toJson(list)
    }
}