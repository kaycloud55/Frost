package com.kaycloud.frost.module.toplist.ui.toplist

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kaycloud.framework.AppExecutors
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
private const val TOP_LIST_ITEMS = "https://www.tophub.fun:8888/GetAllInfoGzip?id=%s"

class TopListRepository private constructor() {

    val topListCategory: MutableLiveData<List<TopListCategory>> = MutableLiveData()
    val topListItems: MutableLiveData<List<ToplistItem>> = MutableLiveData()

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
                AppExecutors.getInstance().getMainThread().execute {
                    topListCategory.value = result.data
                }
            }

        })
    }

    fun getTopListItems(id: String) {
        val request = Request.Builder().url(String.format(TOP_LIST_ITEMS, id)).build()
        NetworkRequester.getOkhttpClient().newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                KLog.e(TAG, "getTopListItems Error:$e")
            }

            override fun onResponse(call: Call, response: Response) {
                KLog.i(TAG, "getTopListItems success")
                if (!response.isSuccessful) throw IOException("response error:$response")
                val listType =
                    object : TypeToken<TopListResponse<ToplistItem>>() {}.type
                val result = Gson().fromJson<TopListResponse<ToplistItem>>(
                    response.body!!.string(),
                    listType
                )
                AppExecutors.getInstance().getMainThread().execute {
                    topListItems.value = result.data
                }
            }

        })
    }
}