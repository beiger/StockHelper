package com.bing.stockhelper.stock.list

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.StockDetail
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.fanhantech.baselib.app.io

class StockListViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        val stockTags = database.loadStockTagsLive()
        val stocksLive = database.loadStocksLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        fun separateTags(tags: List<StockTag>) {
                stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
        }

        @MainThread
        fun delete(item: StockDetail) {
                io { database.deleteStock(item) }
        }
}