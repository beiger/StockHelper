package com.bing.stockhelper.holders.edit

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.StockDetail
import com.fanhantech.baselib.app.io

class HoldEditViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)
        var stockDetail: StockDetail? = null
        var orderDetail: OrderDetail? = null

        @WorkerThread
        fun loadOrder(id: Int) {
                val results = database.loadOrders(id)
                if (results.isNotEmpty()) {
                        orderDetail = results[0]
                }
        }

        @WorkerThread
        fun loadStock(id: Int) {
                val results = database.loadStocks(id)
                if (results.isNotEmpty()) {
                        stockDetail = results[0]
                }
        }



        @MainThread
        fun insert(orderStatus: OrderDetail) {
                io {
                        database.insertOrder(orderStatus)
                }
        }

        @MainThread
        fun update(orderStatus: OrderDetail) {
                io {
                        database.updateOrder(orderStatus)
                }
        }
}