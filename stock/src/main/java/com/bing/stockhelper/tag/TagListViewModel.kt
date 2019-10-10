package com.bing.stockhelper.tag

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.StockTag
import com.fanhantech.baselib.app.io

class TagListViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        val stockTags = database.loadStockTagsLive()

        @MainThread
        fun update(item: StockTag) {
                io { database.updateStockTag(item) }
        }

        @MainThread
        fun delete(item: StockTag) {
                io { database.deleteStockTag(item) }
        }

        @WorkerThread
        fun insertTag(item: StockTag) {
                database.insertStockTag(item)
        }
}