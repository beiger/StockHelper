package com.bing.stockhelper.main.holder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail

class HoldViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)

    val orders = database.loadOrdersLive()
    val dayAttentions = database.loadDayAttentionsLive()

    fun delete(item: OrderDetail) {
        database.deleteOrder(item)
    }
}