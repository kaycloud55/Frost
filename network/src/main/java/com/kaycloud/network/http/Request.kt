package com.kaycloud.network.http

import com.kaycloud.network.CacheStrategy

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 */
abstract class Request<T>(val url: String) : Cloneable {

    private val headers: MutableMap<String, String> = mutableMapOf()
    private val params: Map<String, Any> = mutableMapOf()

    private val cacheKey: String? = null
    private val cacheStrategy: CacheStrategy = CacheStrategy.NET_ONLY


}
