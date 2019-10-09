package com.bing.stockhelper.stock.list

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.StockDetail
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND

class StockDisplayViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        lateinit var stocks: LiveData<List<StockDetail>>
        val stockTagLive = database.loadStockTagsLive()
        var stockTagsFirst: List<StockTag> = listOf()
        var stockTagsSecond: List<StockTag> = listOf()

        fun load(id: Int) {
                stocks = database.loadStocksLive(id)
        }

        @WorkerThread
        fun loadTags() {
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        }
}