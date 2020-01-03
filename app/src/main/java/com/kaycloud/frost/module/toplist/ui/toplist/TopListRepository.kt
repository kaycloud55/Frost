package com.kaycloud.frost.module.toplist.ui.toplist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kaycloud.framework.log.KLog
import com.kaycloud.frost.network.NetworkRequester
import kotlinx.coroutines.CoroutineScope
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

/**
 * author: jiangyunkai
 * Created_at: 2020-01-03
 */

private const val TAG = "TopListRepository"

private const val TOP_LIST_URL = "https://www.tophub.fun:8080/GetType"

class TopListRepository private constructor() {

    val topListCategory: MutableLiveData<List<TopListCategory>> = MutableLiveData()

    companion object {

        @Volatile
        private var instance: TopListRepository? = null

        fun getInstance(): TopListRepository = instance ?: synchronized(this) {
            instance ?: TopListRepository().also {
                instance = it
            }
        }
    }

    fun getTopListCategory() {
        val request = Request.Builder().url(TOP_LIST_URL).build()
        NetworkRequester.getOkhttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                KLog.e(TAG, "getTopListCategory Error:$e")
            }

            override fun onResponse(call: Call, response: Response) {
                KLog.i(TAG, "getTopListCategory success")
                if (!response.isSuccessful) throw IOException("response error:$response")
                val listType =
                    object : TypeToken<TopListResponse<TopListCategory>>() {}.type
                val result = Gson().fromJson<TopListResponse<TopListCategory>>(
                    response.body!!.string(),
                    listType
                )
                topListCategory.value = result.data
            }

        })
    }
}