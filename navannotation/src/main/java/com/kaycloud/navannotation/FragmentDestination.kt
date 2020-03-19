package com.kaycloud.navannotation

/**
 * author: jiangyunkai
 * Created_at: 2020-02-18
 */
@Target(AnnotationTarget.CLASS)
annotation class FragmentDestination(
    val pageUrl: String,
    val needLogin: Boolean,
    val asStarter: Boolean = false
)