package com.fanhantech.baselib.app

import android.app.Application
import android.content.Context

import androidx.multidex.MultiDex
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.squareup.leakcanary.LeakCanary
import com.tencent.bugly.crashreport.CrashReport

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

                if (!AppUtils.isAppDebug()) {
                        initBugly()
                }
        }

        private fun initBugly() {
                val strategy = CrashReport.UserStrategy(applicationContext)
                strategy.appVersion = AppUtils.getAppVersionName()
                strategy.appPackageName = "com.fanhantech.fanhandata"
                CrashReport.initCrashReport(applicationContext, "933a0b47ca", false, strategy)
        }

        companion object {
                @JvmStatic
                lateinit var context: BaseApplication
        }
}
