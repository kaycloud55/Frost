package com.kaycloud.frost.module.image.wallhaven.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.kaycloud.frost.DatabaseConstant
import com.kaycloud.frost.data.RoomListToStringConverters

/**
 * author: kaycloud
 * Created_at: 2019-11-07
 * 默认情况下，Room为实体中定义的每个字段创建一个列。如果有不想持久的字段，可以使用@Ignore来注解。
 * 默认情况下，Room使用类名作为数据库表名。如果希望表具有不同的名称，可以设置@Entity注解的tableName属性。（表名不区分大小写）
 * 默认情况下，Room使用字段名称作为数据库中的列名。如果希望列具有不同的名称，可以使用@ColumnInfo注解字段。
 */

@Entity(tableName = DatabaseConstant.TABLE_WALLHAVEN)
@TypeConverters(ThumbsConverters::class, RoomListToStringConverters::class)
data class WallhavenItemEntity(
    val category: String, // people
    val colors: List<String>,
    val created_at: String, // 2019-11-07 07:17:13
    val dimension_x: Int, // 2560
    val dimension_y: Int, // 1707
    val favorites: Int, // 0
    val file_size: Int, // 1159119
    val file_type: String, // image/jpeg
    @PrimaryKey
    val id: String, // wy3p1x
    val path: String, // https://w.wallhaven.cc/full/wy/wallhaven-wy3p1x.jpg
    val purity: String, // sketchy
    val ratio: String, // 1.5
    val resolution: String, // 2560x1707
    val short_url: String, // https://whvn.cc/wy3p1x
    val source: String,
    val thumbs: Thumbs,
    val url: String, // https://wallhaven.cc/w/wy3p1x
    val views: Int // 0
)

data class Thumbs(
    val large: String, // https://th.wallhaven.cc/lg/wy/wy3p1x.jpg
    val original: String, // https://th.wallhaven.cc/orig/wy/wy3p1x.jpg
    val small: String // https://th.wallhaven.cc/small/wy/wy3p1x.jpg
)

class ThumbsConverters {

    @TypeConverter
    fun stringToThumbs(value: String): Thumbs {
        return Gson().fromJson(value, Thumbs::class.java)
    }

    @TypeConverter
    fun ThumbsToString(thumbs: Thumbs): String {
        return Gson().toJson(thumbs)
    }
}

data class WallhavenTagInfo(
    val alias: String,
    val category: String, // Anime & Manga
    val category_id: Int, // 1
    val created_at: String, // 2014-02-02 10:44:37
    val id: Int, // 1
    val name: String, // anime
    val purity: String // sfw
)

