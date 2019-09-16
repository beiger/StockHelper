package com.bing.stockhelper.app

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.facade.annotation.Route
import com.bing.common_service.app.IApplication

@Route(path = "/stock/StockModule")
class StockModule : IApplication {

        override fun init(context: Context?) {

        }

        override fun attachBaseContext(base: Context) {

        }

        override fun onCreate(application: Application) {
        }
}
