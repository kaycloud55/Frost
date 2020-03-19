package com.kaycloud.navannotation

/**
 * author: jiangyunkai
 * Created_at: 2020-02-18
 * 标记作为Destination的Activity
 */
annotation class ActivityDestination(
    val pageUrl: String,
    val needLogin: Boolean = false,
    val asStarter: Boolean = false
)