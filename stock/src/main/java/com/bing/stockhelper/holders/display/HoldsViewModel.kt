package com.bing.stockhelper.holders.display

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.fanhantech.baselib.app.io

class HoldsViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)
        val orderDetailInfos = database.loadOrderInfosLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        @WorkerThread
        fun loadTags() {
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        }
}