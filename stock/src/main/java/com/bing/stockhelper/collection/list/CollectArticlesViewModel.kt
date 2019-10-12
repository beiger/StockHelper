package com.bing.stockhelper.collection.list

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class CollectArticlesViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        val stockTags = database.loadStockTagsLive()
        val collectArticlesLive = database.loadCollectArticlesLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        fun separateTags(tags: List<StockTag>) {
                stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
        }

        @MainThread
        fun delete(item: CollectArticle) {
                io { database.deleteCollectArticle(item) }
        }
}