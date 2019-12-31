package com.kaycloud.framework.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.WindowManager
import android.widget.Toast
import kotlin.math.roundToInt

/**
 * Created by jiangyunkai on 2019/11/15
 */
/**
 * 获取状态栏高度
 */
fun Context.getStatusBarHeight(): Int {
    var result = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelOffset(resourceId)
    }
    return result
}

fun Context.getScreenHeight(): Int {
    var screenHeight = 0
    val wm = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    wm?.let {
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        screenHeight = metrics.heightPixels
    }
    return screenHeight
}

fun Context.getScreenWidth(): Int {
    var width = 0
    val wm = getSystemService(Context.WINDOW_SERVICE) as? WindowManager
    wm?.let {
        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)
        width = metrics.widthPixels
    }
    return width
}

fun Context.dpToPx(dp: Int): Float {
    val displayMetrics = resources.displayMetrics
    return (dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt().toFloat()
}

fun Context.toast(text: String) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

/**
 * 实化类型参数的示例
 * 调用方式 startActivity<MainActivity>()
 */
inline fun <reified T : Activity> Context.startActivity(extras: Bundle? = null) {
    val intent = Intent(this, T::class.java).apply {
        extras?.let {
            putExtras(it)
        }
    }
    startActivity(intent)
}
