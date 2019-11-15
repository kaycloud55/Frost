package com.kaycloud.framework.ext

/**
 * Created by jiangyunkai on 2019/11/15
 */
fun String.isNotNullOrEmpty(): Boolean {
    return this.isNullOrBlank()
}