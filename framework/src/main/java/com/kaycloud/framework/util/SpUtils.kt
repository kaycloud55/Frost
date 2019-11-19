package com.kaycloud.framework.util

import android.content.Context
import com.kaycloud.framework.BuildConfig
import com.kaycloud.framework.R

/**
 * Created by jiangyunkai on 2019/11/19
 */
object SpUtils {


    fun putInt(context: Context, key: String, value: Int) {
        val sharedPref =
            context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putInt(key, value)
            commit()
        }
    }
}