package com.bing.stockhelper.main.holder

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class HoldViewModel(application: Application) : AndroidViewModel(application) {

        private val database = AppDatabase.getInstance(application)

        val stockTags = database.loadStockTagsLive()
        val orderDetailInfos = database.loadOrderInfosLive()
        val dayAttentions = database.loadDayAttentionsLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        fun seperaterTags(tags: List<StockTag>) {
                stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
        }

        @MainThread
        fun delete(id: Int) {
                io {
                        database.deleteOrder(id)
                }
        }

        @WorkerThread
        fun deleteAllAttention() {
                database.deleteAllAttention()
        }

        @WorkerThread
        fun insert(item: DayAttention) {
                database.insertDayAttention(item)
        }

        @WorkerThread
        fun getStockIdFromFollowId(id: Int): Int? {
                val results = database.loadFollows(id)
                return if (results.isNotEmpty()) {
                        results[0].stockId
                } else {
                        null
                }
        }
}