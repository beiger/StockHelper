package com.bing.stockhelper.main.follow

import android.app.Application
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.ItemFollow
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND

class FollowViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)

    var follows: List<ItemFollow> = listOf()
    val followInfos: MutableList<ItemFollow.Info> = mutableListOf()

    @WorkerThread
    fun loadFollows() {
        follows = database.loadFollows()
        followInfos.clear()
        val stockTags = database.loadStockTags()
        val stockTagsFirst = stockTags.filter { it.level == TAG_LEVEL_FIRST }
        val stockTagsSecond = stockTags.filter { it.level == TAG_LEVEL_SECOND }
        follows.forEach {
            it.toInfo(database, stockTagsFirst, stockTagsSecond)?.let { info ->
                followInfos.add(info)
            }
        }
    }

    fun delete(item: ItemFollow) {
        database.deleteFollow(item)
    }
}