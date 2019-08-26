package com.kaycloud.frost.network.converter

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.TypeAdapter
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import com.kaycloud.frost.data.GankResponse
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * Created by kaycloud on 2019-08-21
 */
class GankResponseBodyConverter<T> constructor(val gson: Gson, val adapter: TypeAdapter<T>) :
    Converter<ResponseBody, String> {

    override fun convert(value: ResponseBody): String? {
        val jsonReader = gson.newJsonReader(value.charStream())
        val adapter = gson.getAdapter(TypeToken.get(GankResponse::class.java))
        value.use { value ->
            val result = adapter.read(jsonReader)
            val results = result.results
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw Throwable("JSON document was not fully consumed.")
            }
            return results
        }
    }


}