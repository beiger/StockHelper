package com.bing.stockhelper.main.follow

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class FollowViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)

    val followInfos = database.loadFollowInfosLive()
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

    fun delete(id: Int) {
        database.deleteFollow(id)
    }
}