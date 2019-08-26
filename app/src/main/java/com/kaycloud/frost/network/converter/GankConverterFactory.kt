package com.kaycloud.frost.network.converter

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * Created by kaycloud on 2019-08-21
 * desc:
 */
class GankConverterFactory private constructor(private val gson: Gson) : Converter.Factory() {

    companion object {
        fun create(): GankConverterFactory {
            return GankConverterFactory(Gson())
        }
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return GankResponseBodyConverter(gson, adapter)
    }


}