package com.kaycloud.framework.ext

import android.view.View

/**
 * Created by jiangyunkai on 2019/11/15
 */
fun View.isVisible() = this.visibility == View.VISIBLE

fun View.isGone() = this.visibility == View.GONE

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun View.showSnackbar(msgId: Int, length: Int) {
//    showSnackbar(context.getString(msgId), length)
}


