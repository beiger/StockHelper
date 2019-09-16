package com.bing.stockhelper.runalone

import android.content.Context
import com.alibaba.android.arouter.launcher.ARouter
import com.bing.stockhelper.app.StockModule
import com.fanhantech.baselib.app.BaseApplication
import com.tamic.novate.util.LogWraper

class StockApplication : BaseApplication() {
        override fun attachBaseContext(base: Context) {
                super.attachBaseContext(base)
                StockModule().attachBaseContext(base)
        }

        override fun onCreate() {
                super.onCreate()
                if (LogWraper.isDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                        ARouter.openLog()    // 打印日志
                        ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                }
                ARouter.init(this) // 尽可能早，推荐在Application中初始化
                StockModule().onCreate(this)
        }
}
