package com.bing.common_service.app

import android.app.Application
import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

interface IApplication: IProvider {
        fun  attachBaseContext(base: Context)

        fun onCreate(application: Application)
}