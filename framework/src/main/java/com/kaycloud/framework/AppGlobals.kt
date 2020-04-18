package com.kaycloud.framework

import android.content.Context

/**
 * author: jiangyunkai
 * Created_at: 2020-02-25
 */

class AppGlobals {
    companion object {
        var sApplicationContext: Context? = null
            get() = field!!
    }
}