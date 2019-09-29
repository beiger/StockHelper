package com.bing.stockhelper.main.holder

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class HoldViewModel(application: Application) : AndroidViewModel(application) {

        private val database = AppDatabase.getInstance(application)

        val orderDetailInfos = database.loadOrderInfosLive()
        val dayAttentions = database.loadDayAttentionsLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        init {
                io { loadTags() }
        }

        @WorkerThread
        fun loadTags() {
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                println("-----${stockTagsFirst.size}")
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        }

        fun delete(id: Int) {
                database.deleteOrder(id)
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