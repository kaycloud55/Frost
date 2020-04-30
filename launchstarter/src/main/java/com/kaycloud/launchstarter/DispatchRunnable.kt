package com.kaycloud.launchstarter

import android.os.Looper
import android.os.Process
import androidx.core.os.TraceCompat

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 */
class DispatchRunnable(private val task: Task) : Runnable {

    override fun run() {
        TraceCompat.beginSection(task.javaClass.simpleName)
        //把task的优先级赋值给当前线程
        Process.setThreadPriority(task.priority())
        //记录初始时间
        var startTime = System.currentTimeMillis()
        //开始等待dependOns的任务执行
        task.isWaiting = true
        task.waitToSatisfy()
        //等待时间
        val waitTime = System.currentTimeMillis() - startTime
        startTime = System.currentTimeMillis()
        //执行task
        task.isRunning = true
        task.run()
        //执行task的尾部任务
        // ....

        if (!task.needCall() || !task.runOnMainThread()) {
            task.isFinished = true

            DispatcherLog.i(task.javaClass.simpleName + " finish")
        }


        TraceCompat.endSection()

    }

    /**
     * 打印出来Task执行的日志
     *
     * @param startTime
     * @param waitTime
     */
    private fun printTaskLog(startTime: Long, waitTime: Long) {
        val runTime = System.currentTimeMillis() - startTime
        if (DispatcherLog.isDebug) {
            DispatcherLog.i(
                task.javaClass.simpleName.toString() + "  wait " + waitTime + "    run "
                        + runTime + "   isMain " + (Looper.getMainLooper() == Looper.myLooper())
                        + "  needWait " + (task.needWait() || Looper.getMainLooper() == Looper.myLooper())
                        + "  ThreadId " + Thread.currentThread().id
                        + "  ThreadName " + Thread.currentThread().name
                        + "  Situation  " + task.getCurrentSituation()
            )
        }
    }

}