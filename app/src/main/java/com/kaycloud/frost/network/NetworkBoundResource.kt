/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kaycloud.frost.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import com.kaycloud.framework.executor.AppTaskExecutor

/**
 * 数据请求中心，处理流程如下：
 *  一旦创建，就会开始新的请求；
 *  1. 判断[shouldFetch]，如果为true，表示需要从网络请求数据；如果false，直接返回数据库中的数据
 *  2. 如果需要请求数据，调用[createCall]发起请求
 *  3. 请求成功之后调用[processResponse]处理请求，并把处理完成的结果通过[saveCallResult]保存到处理
 *  4. 不管是否成功，从数据库更新数据
 *
 * @param <ResultType> 最终UI需要的数据类型
 * @param <RequestType> 请求返回的数据类型：可能是数据库的，也可能是网络直接返回的，这两个要保持一致；
 */
abstract class NetworkBoundResource<ResultType, RequestType>
@MainThread constructor() {
    //从多个数据来源接收结果
    private val result = MediatorLiveData<Resource<ResultType>>()

    init {
        result.value = Resource.loading(null) //初始值loading
        @Suppress("LeakingThis")
        val dbSource = loadFromDb()
        result.addSource(dbSource) { data ->
            result.removeSource(dbSource) //一旦从数据库获取到数据之后就移除这个来源
            //数据库中可能有缓存的数据，但是过期了需要更新
            if (shouldFetch(data)) {
                fetchFromNetwork(dbSource)
            } else {
                result.addSource(dbSource) { newData ->
                    //不需要请求数据，后面的数据都从db获取，所以这里就只监听db
                    setValue(Resource.success(newData))
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private fun fetchFromNetwork(dbSource: LiveData<ResultType>) {
        val apiResponse = createCall() //开始请求数据
        // we re-attach dbSource as a new source, it will dispatch its latest value quickly
        // 先用旧数据填充，拉到新的之后再替换
        result.addSource(dbSource) { newData ->
            setValue(Resource.loading(newData))
        }
        result.addSource(apiResponse) { response ->
            result.removeSource(apiResponse)
            result.removeSource(dbSource)
            when (response) {
                is ApiSuccessResponse -> {
                    //后台处理数据
                    AppTaskExecutor.getInstance().execute {
                        saveCallResult(processResponse(response))
                        AppTaskExecutor.getInstance().executeOnMainThread {
                            // 这里要遵循唯一可信来源，永远只从db获取可信数据
                            result.addSource(loadFromDb()) { newData ->
                                setValue(Resource.success(newData))
                            }
                        }
                    }
                }
                is ApiEmptyResponse -> {
                    AppTaskExecutor.getInstance().executeOnMainThread {
                        // reload from disk whatever we had
                        result.addSource(loadFromDb()) { newData ->
                            setValue(Resource.success(newData))
                        }
                    }
                }
                is ApiErrorResponse -> {
                    onFetchFailed()
                    result.addSource(dbSource) { newData ->
                        setValue(Resource.error(response.errorMessage, newData))
                    }
                }
            }
        }
    }

    protected open fun onFetchFailed() {}

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    @WorkerThread
    protected open fun processResponse(response: ApiSuccessResponse<RequestType>) = response.body

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<ApiResponse<RequestType>>
}