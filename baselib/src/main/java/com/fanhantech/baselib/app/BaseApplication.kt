package com.fanhantech.baselib.app

import android.app.Application
import android.content.Context

import androidx.multidex.MultiDex
import com.blankj.utilcode.util.Utils
import com.squareup.leakcanary.LeakCanary

open class BaseApplication : Application() {

        override fun attachBaseContext(base: Context) {
                super.attachBaseContext(base)
                MultiDex.install(this)
        }

        override fun onCreate() {
                super.onCreate()
                context = this
                initThirdParty()
        }

        private fun initThirdParty() {
                if (!LeakCanary.isInAnalyzerProcess(applicationContext)) {
                        LeakCanary.install(this)
                }
                Utils.init(this)
        }

        companion object {
                @JvmStatic
                lateinit var context: BaseApplication
        }
}
