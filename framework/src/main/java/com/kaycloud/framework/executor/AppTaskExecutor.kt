package com.kaycloud.framework.executor

import androidx.annotation.NonNull
import java.util.concurrent.Executor
import javax.annotation.Nonnull

/**
 * created by jiangyunkai on 2019/8/14.
 * app线程池
 * 分为两类：
 * 1. IO Executor：newFixedThreadPool(4)
 * 2. MainThread Executor：MainLooper AsyncHandler
 *
 * 线程的最佳线程数量：
 * 1.CPU密集型：cpu核心数 + 1
 * 2.IO密集型 ： cpu核心数 * (1 + 平均等待时间/平均运行时间） 平均等待时间也就是IO时间，运行时间也就是CPU时间
 *
 * 外部可以通过调用[setDelegate]方法替换——代理模式
 */
class AppTaskExecutor {

    private var defaultTaskExecutor: TaskExecutor = DefaultTaskExecutor()
    private var delegate: TaskExecutor = defaultTaskExecutor


    /**
     * 暴露给业务的接口，让外部进行线程池实现替换
     * @param taskExecutor 外部实现的线程池
     */
    fun setDelegate(@Nonnull taskExecutor: TaskExecutor) {
        delegate = taskExecutor
    }

    companion object {

        private val sMainThreadExecutor = Executor {
            getInstance()
                .postToMainThread(it)
        }

        private val sIoThreadExecutor = Executor {
            getInstance()
                .execute(it)
        }

        //DCL写法
        @Volatile
        private var instance: AppTaskExecutor? = null

        fun getInstance() = instance
            ?: synchronized(this) {
                instance
                    ?: AppTaskExecutor().also { instance = it }
            }
    }

    fun execute(@NonNull runnable: Runnable) {
        delegate.execute(runnable)
    }

    fun execute(command: () -> Unit) {
        delegate.execute(Runnable { command.invoke() })
    }

    fun executeOnMainThread(@NonNull runnable: Runnable) {
        delegate.executeOnMainThread(runnable)
    }

    fun executeOnMainThread(command: () -> Unit) {
        delegate.executeOnMainThread(Runnable { command.invoke() })
    }

    fun postToMainThread(@Nonnull runnable: Runnable) {
        delegate.postToMainThread(runnable)
    }

    fun postToMainThread(command: () -> Unit) {
        delegate.postToMainThread(Runnable { command.invoke() })
    }

    fun postToMainThreadDelay(command: () -> Unit, delay: Long) {
        delegate.executeOnMainThreadDelay(Runnable { command.invoke() }, delay)
    }

    fun postToMainThreadDelay(command: Runnable, delayMills: Long) {
        delegate.executeOnMainThreadDelay(command, delayMills)
    }
}