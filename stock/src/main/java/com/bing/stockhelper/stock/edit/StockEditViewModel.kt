package com.bing.stockhelper.stock.edit

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class StockEditViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        var stockDetail: StockDetail? = null
        val stockTagLive = database.loadStockTagsLive()
        var stockTagsFirst: List<StockTag> = listOf()
        var stockTagsSecond: List<StockTag> = listOf()

        @WorkerThread
        fun load(id: Int) {
                val results = database.loadStocks(id)
                if (results.isNotEmpty()) {
                        stockDetail = results[0]
                }
        }

        @WorkerThread
        fun loadTags() {
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        }

        @WorkerThread
        fun insertTag(item: StockTag) {
                database.insertStockTag(item)
        }

        @MainThread
        fun insert(item: StockDetail) {
                io {
                        database.insertStock(item)
                }
        }

        @MainThread
        fun update(item: StockDetail) {
                io {
                        database.updateStock(item)
                }
        }


}