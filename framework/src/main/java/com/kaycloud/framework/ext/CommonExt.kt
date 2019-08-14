package com.kaycloud.framework.ext

/**
 * created by jiangyunkai on 2019/8/14.
 */
val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }