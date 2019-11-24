package com.kaycloud.frost.base

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kaycloud.frost.audio.PermissionCallback
import com.orhanobut.logger.Logger

/**
 * Created by jiangyunkai on 2019/11/16
 */
open class BaseActivity : AppCompatActivity() {

    companion object {
        const val PERMISSION_REQUEST_CODE = 100
    }

    private var mPermissionCallback: PermissionCallback? = null

    /**
     * 权限检查
     */
    fun checkPermission(permission: String, callback: PermissionCallback) {
        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
//
//            } else {
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(permission),
//                    PERMISSION_REQUEST_CODE
//                )
//                mPermissionCallback = callback
//            }
            Logger.t("checkPermission").i("无权限")
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                PERMISSION_REQUEST_CODE
            )
            mPermissionCallback = callback
        } else {
            callback.onRequestPermissionsResult(
                PERMISSION_REQUEST_CODE, arrayOf(permission),
                IntArray(PackageManager.PERMISSION_GRANTED)
            )
            Logger.t("checkPermission").i("有权限")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        mPermissionCallback?.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    /**
     * 沉浸式状态栏
     */
    protected fun setImmersive() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View
            .SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
            window.navigationBarColor = Color.TRANSPARENT
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}