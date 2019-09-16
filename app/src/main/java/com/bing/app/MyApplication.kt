package com.bing.app

import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.launcher.ARouter
import com.bing.common_service.app.IApplication
import com.blankj.utilcode.util.AppUtils
import com.fanhantech.baselib.app.BaseApplication

class MyApplication : BaseApplication() {

        @Autowired(name = "/stock/StockModule")
        @JvmField
        var stockModule: IApplication? = null

        override fun onCreate() {
                super.onCreate()


                if (AppUtils.isAppDebug()) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
                        ARouter.openLog()    // 打印日志
                        ARouter.openDebug()   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
                }
                ARouter.init(this) // 尽可能早，推荐在Application

                ARouter.getInstance().inject(this)

                stockModule?.onCreate(this)
        }
}