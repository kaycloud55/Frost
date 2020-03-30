package com.kaycloud.framework.executor

import androidx.annotation.NonNull
import javax.annotation.Nonnull

/**
 * author: jiangyunkai
 * Created_at: 2020/3/19
 */
abstract class TaskExecutor {

    abstract fun postToMainThread(@NonNull command: Runnable)

    abstract fun executeOnDiskIO(@Nonnull command: Runnable)

    fun executeOnMainThread(@Nonnull command: Runnable) {
        if (isMainThread()) {
            command.run()
        } else {
            postToMainThread(command)
        }
    }

    abstract fun isMainThread(): Boolean
}