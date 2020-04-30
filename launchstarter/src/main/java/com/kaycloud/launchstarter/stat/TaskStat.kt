package com.kaycloud.launchstarter.stat

import com.kaycloud.launchstarter.TaskStatBean
import java.util.concurrent.atomic.AtomicInteger

/**
 * author: jiangyunkai
 * Created_at: 2020/4/30
 */
object TaskStat {

    @Volatile
    private var currentSituation = ""

    private val beans: MutableList<TaskStatBean> = mutableListOf()


    private val taskDoneCount: AtomicInteger = AtomicInteger(0)

    fun markTaskDone() {
        taskDoneCount.getAndIncrement()
    }

}