package com.kaycloud.framework.ext

import android.view.View
import com.google.android.material.snackbar.Snackbar

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

fun View.showSnackbar(
    text: String,
    timeLength: Int,
    onShown: ((sb: Snackbar?) -> Unit)? = null,
    onDismissed: ((sb: Snackbar?, event: Int) -> Unit)? = null
) {
    Snackbar.make(this, text, timeLength).run {
        addCallback(object : Snackbar.Callback() {
            override fun onShown(sb: Snackbar?) {
                onShown?.invoke(sb)
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                onDismissed?.invoke(transientBottomBar, event)
            }
        })
    }
}


