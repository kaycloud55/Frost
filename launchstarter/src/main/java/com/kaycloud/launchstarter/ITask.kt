package com.kaycloud.launchstarter

import android.os.Process
import androidx.annotation.IntRange
import java.util.concurrent.Executor

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 */
interface ITask {

    fun run()

    /**
     * @return 返回的是Class，因为在编写代码的时候是无法直接拿到实例的
     */
    fun dependOns(): List<Class<in Task>>?

    @IntRange(
        from = Process.THREAD_PRIORITY_BACKGROUND.toLong(),
        to = Process.THREAD_PRIORITY_LOWEST.toLong()
    )
    fun priority(): Int

    fun runOn(): Executor

    /**
     * 是否在主线程执行
     */
    fun runOnMainThread() = true

    /**
     * 只在主进程执行
     */
    fun onlyRunInMainProcess() = true


}