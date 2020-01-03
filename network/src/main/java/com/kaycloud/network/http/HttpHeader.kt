package com.kaycloud.network.http

import java.util.HashMap

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 */
//class HttpHeader : NameValueMap<String, String> {
//
//    val ACCEPT = "Accept"
//    val PRAGMA = "Pragma"
//    val PROXY_CONNECTION = "Proxy-Connection"
//    val USER_AGENT = "User-Agent"
//    val ACCEPT_ENCODING = "accept-encoding"
//    val CACHE_CONTROL = "Cache-Control"
//    val CONTENT_ENCODING = "Content-Encoding"
//    val CONNECTION = "Connection"
//    val CONTENT_LENGTH = "Content-length"
//
//    val CONTENT_TYPE = "Content-Type"
//
//    private val mMap = HashMap<String, String>()
//
//
//    override fun get(name: String): String? = mMap.get(name)
//    override fun get(key: String): String? = mMap[key]
//    override fun set(name: String, value: String) = mMap.set(name, value)
//    override fun setAll(map: Map<String, String>) = mMap.putAll(map)
//    override fun containsKey(key: String): Boolean = mMap.containsKey(key)
//    override fun containsValue(value: String): Boolean = mMap.containsValue(value)
//    override fun isEmpty(): Boolean = mMap.isEmpty()
//
//}