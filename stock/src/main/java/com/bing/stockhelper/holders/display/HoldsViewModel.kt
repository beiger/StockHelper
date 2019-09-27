package com.bing.stockhelper.holders.display

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND

class HoldsViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)
        var orders: MutableList<OrderDetail> = mutableListOf()
        val orderDetailInfo: MutableList<OrderDetail.DetailInfo> = mutableListOf()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        @WorkerThread
        fun loadOrders() {
                orders.clear()
                orders.addAll(database.loadOrders())
                orderDetailInfo.clear()
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
                orders.forEach {
                        it.toInfo(database, stockTagsFirst, stockTagsSecond)?.let { info ->
                                orderDetailInfo.add(info)
                        }
                }
        }

        @WorkerThread
        fun update(position: Int) {
                val orderDetails = database.loadOrders(orders[position].id)
                if (orderDetails.isNotEmpty()) {
                        val orderDetail = orderDetails[0]
                        orders[position] = orderDetail
                        orderDetail.toInfo(database, stockTagsFirst, stockTagsSecond)?.let {
                                orderDetailInfo[position] = it
                        }
                }
        }
}