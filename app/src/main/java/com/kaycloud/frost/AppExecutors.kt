package com.kaycloud.frost

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * created by jiangyunkai on 2019/8/14.
 * Java Executor框架
 */
class AppExecutors {

    private val diskIO: Executor
    private val networkIO: Executor
    private val mainThread: Executor

    init {

        diskIO = Executors.newSingleThreadExecutor()
        networkIO = Executors.newFixedThreadPool(3)
        mainThread = MainThreadExecutor()
    }

    fun getDiskIO() = diskIO

    fun getNetworkIO() = networkIO

    fun getMainThread() = mainThread

    companion object {

        @Volatile
        private var instance: AppExecutors? = null

        fun getInstance() = instance ?: synchronized(this) {
            instance ?: AppExecutors().also { instance = it }
        }
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable?) {
            mainThreadHandler.post(command)
        }
    }
}