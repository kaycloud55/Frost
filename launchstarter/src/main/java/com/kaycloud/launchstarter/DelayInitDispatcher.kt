package com.kaycloud.launchstarter

import android.os.Looper
import android.os.MessageQueue
import java.util.*

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 * 延迟启动器，利用MessageQueue的IdleHandler的特性
 */
class DelayInitDispatcher {

    private val mDelayTasks = LinkedList<Task>()

    private val mIdleHandler = MessageQueue.IdleHandler {
        if (mDelayTasks.isNotEmpty()) {
            val task = mDelayTasks.poll()
            DispatchRunnable(task).run()
        }
        return@IdleHandler mDelayTasks.isNotEmpty()
    }

    fun addTask(task: Task): DelayInitDispatcher {
        mDelayTasks.add(task)
        return this
    }

    fun start() {
        Looper.myQueue().addIdleHandler(mIdleHandler)
    }
}