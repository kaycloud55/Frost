package com.kaycloud.frost.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * author: kaycloud
 * Created_at: 2019-11-08
 * Room数据库手动转换
 */

class RoomListToStringConverters {

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