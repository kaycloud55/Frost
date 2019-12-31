package com.kaycloud.network.http

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 */
interface NameValueMap<K, V> : Map<K, V> {
    fun get(name: String): String

    fun set(name: String, value: String)

    fun setAll(map: Map<String, String>)
}