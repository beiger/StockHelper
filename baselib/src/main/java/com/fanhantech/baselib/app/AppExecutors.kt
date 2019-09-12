package com.fanhantech.baselib.app

import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.*

import java.util.concurrent.Executor
import java.util.concurrent.Executors

/**
 * 协程防止内存泄漏：
 * 1. 在协程内部使用view的弱引用；
 * 2. 传入job，在distroy中取消。
 */
object AppExecutors {
        val diskIO: Executor = Executors.newSingleThreadExecutor()
        val networkIO: Executor = Executors.newFixedThreadPool(3)
        val mainThread: Executor = MainThreadExecutor()

        val ioScope = CoroutineScope(Dispatchers.IO)
        val uiScope = CoroutineScope(Dispatchers.Main)

        private class MainThreadExecutor : Executor {
                private val mainThreadHandler = Handler(Looper.getMainLooper())

                override fun execute(command: Runnable) {
                        mainThreadHandler.post(command)
                }
        }
}

fun ui(
        job: Job? = null,
        block: (suspend CoroutineScope.() -> Unit)
): Job = if (job == null) {
                AppExecutors.uiScope.launch {
                        block()
                }
        } else {
                CoroutineScope(Dispatchers.Main + job).launch {
                        block()
                }
        }

fun io(
        job: Job? = null,
        block: (suspend CoroutineScope.() -> Unit)
): Job = if (job == null) {
        AppExecutors.ioScope.launch {
                block()
        }
} else {
        CoroutineScope(Dispatchers.IO + job).launch {
                block()
        }
}

suspend fun <T> waitUI(
        block: suspend CoroutineScope.() -> T
): T {
        return withContext(AppExecutors.uiScope.coroutineContext) {
                block()
        }
}

suspend fun <T> waitIO(
        block: suspend CoroutineScope.() -> T
): T {
        return withContext(AppExecutors.ioScope.coroutineContext) {
                block()
        }
}
