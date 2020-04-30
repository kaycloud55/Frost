package com.kaycloud.framework.executor

import android.os.Build
import android.os.Handler
import android.os.Looper
import okhttp3.internal.threadFactory
import java.lang.reflect.InvocationTargetException
import java.util.concurrent.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.concurrent.thread
import kotlin.math.max
import kotlin.math.min

/**
 * author: jiangyunkai
 * Created_at: 2020/3/19
 *
 */
class DefaultTaskExecutor : TaskExecutor() {

    companion object {

        /**
         * CPU核数
         */
        private val CPU_COUNT = Runtime.getRuntime().availableProcessors()

        /**
         * 线程池核心线程数
         */
        private val CORE_POOL_SIZE = max(2, min(CPU_COUNT - 1, 5))

        /**
         * 线程池最大线程数
         */
        private val MAX_POOL_SIZE = CORE_POOL_SIZE

        /**
         * 核心线程Idle存活时间
         */
        private const val KEEP_ALIVE_SECONDS = 5L

        private val poolWorkQueue = LinkedBlockingDeque<Runnable>()

        /**
         * CPU 密集型任务线程池
         */
        val cpuThreadPoolExecutor by lazy {
            ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                poolWorkQueue,
                threadPoolFactory,
                rejectHandler
            ).apply {
                allowCoreThreadTimeOut(true)
            }
        }

        val ioThreadPoolExecutor by lazy {
            Executors.newCachedThreadPool(threadPoolFactory)
        }

        val scheduledThreadPoolExecutor by lazy {
            Executors.newScheduledThreadPool(1, threadPoolFactory)
        }

        private val rejectHandler =
            RejectedExecutionHandler { r, executor -> Executors.newCachedThreadPool().execute(r) }


        private val poolNumber = AtomicInteger(1)

        private val threadPoolFactory = DefaultThreadFactory()

        class DefaultThreadFactory : ThreadFactory {
            private var group: ThreadGroup
            private var namePrefix: String
            private var threadNumber = AtomicInteger(1)

            init {
                val securityManager = System.getSecurityManager()
                group = securityManager?.threadGroup ?: Thread.currentThread().threadGroup
                namePrefix = "DefaultTaskExecutor-${poolNumber.getAndIncrement()}-Thread-"
            }

            override fun newThread(r: Runnable?): Thread {
                val thread = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0)
                if (thread.isDaemon) {
                    thread.isDaemon = false
                }
                if (thread.priority != Thread.NORM_PRIORITY) {
                    thread.priority = Thread.NORM_PRIORITY
                }
                return thread
            }

        }
    }

    private val mLock = Any()


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
        ioThreadPoolExecutor.execute(command)
    }

    override fun executeDelay(command: Runnable, delayMills: Long) {
        scheduledThreadPoolExecutor.schedule(command, delayMills, TimeUnit.MILLISECONDS)
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

    override fun getCpuThreadPoolExecutor(): Executor {
        return scheduledThreadPoolExecutor
    }

}