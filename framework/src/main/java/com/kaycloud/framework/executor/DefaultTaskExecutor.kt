package com.kaycloud.framework.executor

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread

/**
 * author: jiangyunkai
 * Created_at: 2020/3/19
 *
 */
class DefaultTaskExecutor : TaskExecutor() {

    companion object {
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()
    }

    private val mLock = Any()

    private val mScheduler = Executors.newScheduledThreadPool(CPU_COUNT, object : ThreadFactory {
        private val THREAD_NAME_STEM = "app_scheduler_thread_%d"
        private val mThreadId = AtomicInteger(0)

        override fun newThread(r: Runnable?): Thread {
            return thread(
                name = String.format(THREAD_NAME_STEM, mThreadId.incrementAndGet())
            ) {
                r?.run()
            }
        }

    })

    private val mExecutor =
        Executors.newFixedThreadPool(4, object : ThreadFactory {
            private val THREAD_NAME_STEM = "app_disk_io_thread_%d"
            private val mThreadId = AtomicInteger(0)

            override fun newThread(r: Runnable): Thread {
                val t = Thread(r)
                t.name = String.format(THREAD_NAME_STEM, mThreadId.getAndIncrement())
                return t
            }
        })

    @Volatile
    private var mMainHandler: Handler? = null

    private fun getMainThreadHandler(): Handler {
        return mMainHandler ?: synchronized(mLock) {
            mMainHandler ?: createAsync(Looper.getMainLooper()).also { mMainHandler = it }
        }
    }

    override fun postToMainThread(command: Runnable) {
        getMainThreadHandler().post(command)
    }

    override fun executeOnMainThreadDelay(command: Runnable, delayMills: Long) {
        getMainThreadHandler().postDelayed(command, delayMills)
    }

    override fun execute(command: Runnable) {
        mExecutor.execute(command)
    }

    override fun executeDelay(command: Runnable, delayMills: Long) {
        mScheduler.schedule(command, delayMills, TimeUnit.MILLISECONDS)
    }

    override fun isMainThread() = Looper.getMainLooper().thread == Thread.currentThread()

    /**
     * 创建异步Handler
     */
    private fun createAsync(looper: Looper): Handler {
        if (Build.VERSION.SDK_INT >= 28) {
            return Handler.createAsync(looper) //同一个Handler内的message会按顺序执行，不同的就不能保证
        }
        if (Build.VERSION.SDK_INT >= 16) {
            try {
                return Handler::class.java.getDeclaredConstructor(
                    Looper::class.java,
                    Handler.Callback::class.java,
                    Boolean::class.javaPrimitiveType
                ).newInstance(looper, null, true)
            } catch (ignored: IllegalAccessException) {
            } catch (ignored: InstantiationException) {
            } catch (ignored: NoSuchMethodException) {
            } catch (e: InvocationTargetException) {
                return Handler(looper)
            }
        }
        return Handler(looper)
    }


}