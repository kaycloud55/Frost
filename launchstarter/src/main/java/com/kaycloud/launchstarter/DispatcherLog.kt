package com.kaycloud.launchstarter

import android.util.Log

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 */
class DispatcherLog {

    companion object {

        var isDebug = true

        fun i(msg: String) {
            Log.i("TaskDispatcher", msg)
        }
    }
}