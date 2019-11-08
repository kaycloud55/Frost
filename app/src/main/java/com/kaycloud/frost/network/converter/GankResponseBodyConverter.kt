package com.kaycloud.frost.network.converter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonToken
import com.kaycloud.frost.BuildConfig
import com.orhanobut.logger.Logger
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Converter
import java.io.ByteArrayInputStream
import java.io.InputStreamReader


/**
 * Created by kaycloud on 2019-08-21
 */
class GankResponseBodyConverter<T> constructor(
    private val gson: Gson,
    private val adapter: TypeAdapter<T>
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? {
        value.use { value ->
            val responseString = value.string() //把responseBody转为string
            val jsonObject = JSONObject(responseString)
            var jsonResult = jsonObject.optString("results")
            if (jsonResult.isNullOrEmpty()) {
                jsonResult = jsonObject.optString("data")
            }
            if (BuildConfig.DEBUG) {
                Logger.t(BuildConfig.APPLICATION_ID).d(responseString)
            }
            val inputStream = ByteArrayInputStream(jsonResult.toByteArray())
            val jsonReader = gson.newJsonReader(InputStreamReader(inputStream))
            val result = adapter.read(jsonReader)
            if (jsonReader.peek() != JsonToken.END_DOCUMENT) {
                throw Throwable("JSON document was not fully consumed.")
            }
            return result
        }
    }
}