package com.kaycloud.frost.extension

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * author: kaycloud
 * Created_at: 2019-08-24
 */

val Context.networkInfo: NetworkInfo?
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

val Context.packageInfo: PackageInfo?
    get() = try {
        this.packageManager.getPackageInfo(
            this.packageName, 0
        )
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

//region copy paste
/**
 * 实现文本复制功能
 * add by wangqianzhou
 * @param content
 */
fun Context.copy(content: String) {
    // 得到剪贴板管理器
    val cmb = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    cmb.primaryClip = ClipData.newPlainText(null, content)
}

/**
 * 实现粘贴功能
 * add by wangqianzhou
 * *
 * @return
 */
fun Context.paste(): String? {
    // 得到剪贴板管理器
    val cmb = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    return cmb.primaryClip?.let {
        if (it.itemCount > 0) it.getItemAt(0).coerceToText(this)?.toString() else null
    }
}

//region screenWidth
val Context.screenWidth: Int
    inline get() {
        val wm = this.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        wm.defaultDisplay.getMetrics(this.displayMetrics)
        return displayMetrics.widthPixels
    }

val Context.displayMetrics: DisplayMetrics
    get() = resources.displayMetrics


fun Context.dpToPx(dp: Float) = (dp * this.displayMetrics.density + 0.5).toInt()

fun Context.pxToDp(px: Float) = (px / this.displayMetrics.density + 0.5).toInt()