package com.kaycloud.framework.executor

import androidx.annotation.NonNull
import java.util.concurrent.Executor
import javax.annotation.Nonnull

/**
 * author: jiangyunkai
 * Created_at: 2020/3/19
 */
abstract class TaskExecutor {

    abstract fun postToMainThread(@NonNull command: Runnable)

    abstract fun executeOnMainThreadDelay(@NonNull command: Runnable, delayMills: Long)

    abstract fun execute(@Nonnull command: Runnable)

    abstract fun executeDelay(@NonNull command: Runnable, delayMills: Long)

    /**
     * 如果当前是在主线程，会比[postToMainThread]快一点
     */
    fun executeOnMainThread(@Nonnull command: Runnable) {
        if (isMainThread()) {
            command.run()
        } else {
            postToMainThread(command)
        }
    }

    abstract fun isMainThread(): Boolean

    abstract fun getCpuThreadPoolExecutor(): Executor
}