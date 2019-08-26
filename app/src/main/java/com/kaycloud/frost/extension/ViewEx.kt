package com.kaycloud.frost.extension

import android.view.View

/**
 * author: kaycloud
 * Created_at: 2019-08-24
 * desc: View的扩展函数
 */

fun View.isVisible() = this.visibility == View.VISIBLE

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}