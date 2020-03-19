package com.kaycloud.network

/**
 * author: jiangyunkai
 * Created_at: 2020-03-03
 * 网络请求缓存策略
 */
enum class CacheStrategy {
    CACHE_ONLY, // 只访问本地缓存
    CACHE_FIRST, // 优先访问本地缓存，如果没有就发起请求
    NET_ONLY, // 绕过缓存，直接发起请求
    NET_CACHE // 先访问网络，成功后缓存到本地
}