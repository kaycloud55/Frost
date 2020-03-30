package com.kaycloud.framework.executor

import android.os.Build
import android.os.Handler
import android.os.Looper
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * author: jiangyunkai
 * Created_at: 2020/3/19
 *
 */
class DefaultTaskExecutor : TaskExecutor() {

    private val mLock = Any()

    private val mDiskIO =
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

    override fun postToMainThread(command: Runnable) {
        mMainHandler ?: synchronized(mLock) {
            mMainHandler ?: createAsync(Looper.getMainLooper()).also { mMainHandler = it }
        }
        mMainHandler!!.post(command)
    }

    override fun executeOnDiskIO(command: Runnable) {
        mDiskIO.execute(command)
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