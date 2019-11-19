package com.kaycloud.framework.log

import com.kaycloud.framework.BuildConfig
import com.orhanobut.logger.Logger

/**
 * Created by jiangyunkai on 2019/11/19
 */
object KLog {


    fun v(tag: String, message: String, vararg args: Any) {
        if (isLoggable()) {
            Logger.t(tag).v(message, args)
        }
    }

    fun d(tag: String, message: String, vararg args: Any) {
        if (isLoggable()) {
            Logger.t(tag).d(message, args)
        }
    }

    fun i(tag: String, message: String, vararg args: Any) {
        if (isLoggable()) {
            Logger.t(tag).i(message, args)
        }
    }

    fun w(tag: String, message: String, vararg args: Any) {
        if (isLoggable()) {
            Logger.t(tag).w(message, args)
        }
    }

    fun e(tag: String, message: String, vararg args: Any) {
        if (isLoggable()) {
            Logger.t(tag).e(message, args)
        }
    }

    fun wtf(tag: String, message: String, vararg args: Any) {
        if (isLoggable()) {
            Logger.t(tag).wtf(message, args)
        }
    }

    fun xml(tag: String, xml: String?) {
        Logger.t(tag).xml(xml)
    }

    fun json(tag: String, json: String?) {
        Logger.t(tag).json(json)
    }

    private fun isLoggable(): Boolean {
        return BuildConfig.DEBUG
    }


}