package com.bing.stockhelper.main.holder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
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

        fun delete(id: Int) {
                database.deleteOrder(id)
        }

        fun deleteAllAttention() {
                database.deleteAllAttention()
        }

        fun insert(item: DayAttention) {
                database.insertDayAttention(item)
        }
}