package com.bing.stockhelper.collection.detail.article

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class CollectArticleViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        var collectArticle: CollectArticle? = null
        val stockTagLive = database.loadStockTagsLive()
        var stockTagsFirst: List<StockTag> = listOf()
        var stockTagsSecond: List<StockTag> = listOf()

        @WorkerThread
        fun load(id: Int) {
                val results = database.loadCollectArticles(id)
                if (results.isNotEmpty()) {
                        collectArticle = results[0]
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
        fun insert(item: CollectArticle) {
                io {
                        database.insertCollectArticle(item)
                }
        }

        @MainThread
        fun update(item: CollectArticle) {
                io {
                        database.updateCollectArticle(item)
                }
        }
}
