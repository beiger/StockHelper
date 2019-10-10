package com.bing.stockhelper.collection.display

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*

class ArticleDisplayViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        lateinit var articles: LiveData<List<CollectArticle>>
        val stockTagLive = database.loadStockTagsLive()
        var stockTagsFirst: List<StockTag> = listOf()
        var stockTagsSecond: List<StockTag> = listOf()

        fun load(id: Int) {
                articles = database.loadCollectArticlesLive(id)
        }

        @WorkerThread
        fun loadTags() {
                val stockTags = database.loadStockTags()
                stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        }
}