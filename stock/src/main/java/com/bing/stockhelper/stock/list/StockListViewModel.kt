package com.bing.stockhelper.stock.list

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.StockDetail
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.app.ui

class StockListViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        val stocksLive = database.loadStocksLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        init {
                io { loadTags() }
        }

        @WorkerThread
        fun loadTags() {
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        }

        @MainThread
        fun delete(item: StockDetail) {
                ui { database.deleteStock(item) }
        }
}