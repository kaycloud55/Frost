package com.kaycloud.launchstarter

import android.os.Process
import com.kaycloud.framework.executor.AppTaskExecutor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 */
class Task : ITask {

    @Volatile
    var isWaiting = false

    @Volatile
    var isRunning = false

    @Volatile
    var isFinished = false

    @Volatile
    var isDispatched = false

    /**
     * 当前任务依赖控制
     */
    private var dependsLatch =
        if (dependOns() != null && dependOns()!!.isNotEmpty()) CountDownLatch(dependOns()!!.size) else null

    /**
     * 等待依赖任务全部完成
     */
    fun waitToSatisfy() {
        try {
            dependsLatch?.await()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    /**
     * 依赖的任务完成一个
     */
    fun satisfy() {
        dependsLatch?.countDown()
    }

    /**
     * 针对优先级一般，但是非常耗时的任务，需要尽早让它开始，不至于拖慢整体的速度
     */
    fun needRunAsSoon(): Boolean {
        return false
    }


    override fun run() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun dependOns(): List<Class<Task>>? {
        return null
    }


    /**
     * 对于异步任务：是否需要等待countDownLatch，默认不需要
     */
    fun needWait() = false

    /**
     * 运行在主线程的任务不要更改优先级
     */
    override fun priority() = Process.THREAD_PRIORITY_BACKGROUND

    /**
     * 默认运行在IO线程池
     * CPU密集的任务要切换到[AppTaskExecutor.Companion.sMainThreadExecutor]
     */
    override fun runOn(): Executor {
        return AppTaskExecutor.sIoThreadExecutor
    }

    fun needCall() = false

}