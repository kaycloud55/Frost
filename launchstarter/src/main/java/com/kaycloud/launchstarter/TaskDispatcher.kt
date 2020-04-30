package com.kaycloud.launchstarter

import android.content.Context
import android.os.Looper
import androidx.annotation.UiThread
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 */
class TaskDispatcher() {

    private var allTasks = mutableListOf<Task>()
    private val allTaskClz = mutableListOf<Class<Task>>()

    private val mainThreadTasks = arrayListOf<Task>()

    private var countDownLatch: CountDownLatch? = null
    private var startTime: Long = -1

    companion object {

        @Volatile
        private var hasInit: Boolean = false
        private lateinit var context: Context

        fun init(context: Context) {
            this.context = context
            hasInit = true
        }

        fun createInstance(): TaskDispatcher {
            if (!hasInit) {
                throw IllegalStateException("call init() first!!!")
            }
            return TaskDispatcher()
        }
    }

    fun addTask(task: Task): TaskDispatcher {
        allTasks.add(task)
        allTaskClz.add(task.javaClass)
        if (isNeedWait(task)) {
            needWaitTasks.add(task)
            needWaitCount.getAndIncrement()


        }
        return this
    }

    @UiThread
    fun start() {
        startTime = System.currentTimeMillis()
        if (Looper.getMainLooper() != Looper.myLooper()) {
            throw RuntimeException("must be called from UiThread")
        }
        if (allTasks.isNotEmpty()) {
            analyseCount.getAndIncrement()

            allTasks = TaskSortUtil.getSortResult(allTasks, allTaskClz).toMutableList()
            countDownLatch = CountDownLatch(needWaitCount.get())

            sendAndExecuteAsyncTasks()
        }
    }

    private fun sendAndExecuteAsyncTasks() {
        for (task in allTasks) {
            if (task.onlyRunInMainProcess()) {
                markTaskDone(task)
            } else {
                sendTaskReal()
            }
            task.isDispatched = true
        }
    }

    private fun sendTaskReal(task: Task) {
        if (task.runOnMainThread()) {
            mainThreadTasks.add(task)
            if (task.needCall()) {

            }
        } else {

        }
    }

    private fun markTaskDone(task: Task) {
        if (isNeedWait(task)) {
            finishedTasks.add(task.javaClass)
            needWaitTasks.remove(task)
            countDownLatch?.countDown()
            needWaitCount.getAndDecrement()
        }

    }

    /**
     * 主线程需要等待的任务数
     */
    private val needWaitCount = AtomicInteger(0)

    private val needWaitTasks: MutableList<Task> = mutableListOf()

    private val finishedTasks: MutableList<Class<Task>> = mutableListOf()

    private val dependsOnMap = hashMapOf<Class<Task>, MutableList<Task>>()

    /**
     * 分析依赖的次数，统计分析耗时
     */
    private val analyseCount = AtomicInteger(0)

    fun collectDepends(task: Task) {
        if (task.dependOns() != null && task.dependOns()!!.isNotEmpty()) {
            for (taskClz in task.dependOns()!!) {
                if (dependsOnMap[taskClz] == null) {
                    dependsOnMap[taskClz] = mutableListOf()
                }
                dependsOnMap[taskClz]?.add(task)
                if (finishedTasks.contains(taskClz)) {
                    task.satisfy()
                }

            }
        }
    }

    private fun isNeedWait(task: Task): Boolean {
        return !task.runOnMainThread() && task.needWait()
    }

    /**
     * 查看被依赖的信息
     */
    private fun printDependedMsg() {
        DispatcherLog.i("needWait size : " + needWaitCount.get())
        if (false) {
            for (cls in dependsOnMap.keys) {
                DispatcherLog.i("cls " + cls.simpleName + "   " + dependsOnMap[cls]?.size)
                for (task in dependsOnMap.get(cls)!!) {
                    DispatcherLog.i("cls       " + task.javaClass.simpleName)
                }
            }
        }
    }

    @UiThread
    private fun await() {
        try {
            if (needWaitCount.get() > 0) {
                if (countDownLatch == null) {
                    throw RuntimeException("have to call start() before call await")
                }
                countDownLatch?.await(1000 * 10, TimeUnit.MILLISECONDS)
            }
        } catch (e: InterruptedException) {

        }
    }

}