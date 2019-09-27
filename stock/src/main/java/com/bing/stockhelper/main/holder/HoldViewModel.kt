package com.bing.stockhelper.main.holder

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.DayAttention
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND

class HoldViewModel(application: Application) : AndroidViewModel(application) {

        private val database = AppDatabase.getInstance(application)

        var orders: List<OrderDetail> = listOf()
        val orderDetailInfo: MutableList<OrderDetail.DetailInfo> = mutableListOf()
        val dayAttentions = database.loadDayAttentionsLive()

        @WorkerThread
        fun loadOrders() {
                orders = database.loadOrders()
                orderDetailInfo.clear()
                val stockTags = database.loadStockTags()
                val stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                val stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
                orders.forEach {
                        it.toInfo(database, stockTagsFirst, stockTagsSecond)?.let { info ->
                                orderDetailInfo.add(info)
                        }
                }
        }

        fun delete(item: OrderDetail) {
                database.deleteOrder(item)
        }

        fun deleteAllAttention() {
                database.deleteAllAttention()
        }

        fun insert(item: DayAttention) {
                database.insertDayAttention(item)
                val attentions = database.loadDayAttentions()
                println("-------${attentions.size}")
        }
}