package com.kaycloud.network.http.base

import com.kaycloud.network.http.HttpHeader

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 */
interface Header {
    fun getHeaders(): HttpHeader
}