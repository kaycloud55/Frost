package com.kaycloud.network.http

import java.net.URI

/**
 * author: jiangyunkai
 * Created_at: 2019-12-31
 */
interface Request {

    fun getHeader()

    fun getBody()

    fun getMethod(): Method

    fun getUri(): URI
}