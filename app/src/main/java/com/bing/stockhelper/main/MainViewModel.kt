package com.bing.stockhelper.main

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail
import com.fanhantech.baselib.app.io

class MainViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        @MainThread
        fun insert(orderStatus: OrderDetail) {
                io {
                        database.insertOrder(orderStatus)
                        println("-----${database.loadOrders().size}")
                }
        }
}