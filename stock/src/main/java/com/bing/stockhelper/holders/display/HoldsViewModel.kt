package com.bing.stockhelper.holders.display

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail

class HoldsViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)
        val orders = database.loadOrdersLive()

        var ordersThisOpen: MutableList<OrderDetail> = mutableListOf()
}