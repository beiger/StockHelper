package com.bing.stockhelper.main.follow

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND

class FollowViewModel(application: Application) : AndroidViewModel(application) {

        private val database = AppDatabase.getInstance(application)

        val stockTags = database.loadStockTagsLive()
        val followInfos = database.loadFollowInfosLive()
        var stockTagsFirst = listOf<StockTag>()
        var stockTagsSecond = listOf<StockTag>()

        fun separateTags(tags: List<StockTag>) {
                stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
        }

        @WorkerThread
        fun delete(id: Int) {
                database.deleteFollow(id)
        }

        @WorkerThread
        fun getStockIdFromFollowId(id: Int): Int? {
                val results = database.loadFollows(id)
                return if (results.isNotEmpty()) {
                        results[0].stockId
                } else {
                        null
                }
        }
}