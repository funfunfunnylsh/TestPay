package com.dsx.easypay.util

import java.util.concurrent.Executors

/**
 * Description: 简易线程池管理工具.
 */
object ThreadManager {
    private var mExecutors = Executors.newSingleThreadExecutor()
    @JvmStatic
    fun execute(runnable: Runnable?) {
        if (mExecutors == null) {
            mExecutors = Executors.newSingleThreadExecutor()
        }
        mExecutors!!.execute(runnable)
    }

    @JvmStatic
    fun shutdown() {
        if (mExecutors == null || mExecutors!!.isShutdown) {
            return
        }
        mExecutors!!.shutdownNow()
        mExecutors = null
    }
}