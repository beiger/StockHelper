package com.bing.stockhelper.follow

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.ItemFollow
import com.bing.stockhelper.model.entity.StockDetail
import com.fanhantech.baselib.app.io

class FollowEditViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)
        var itemFollow: ItemFollow? = null
        var stockDetail: StockDetail? = null

        @WorkerThread
        fun loadFollow(id: Int) {
                val results = database.loadFollows(id)
                if (results.isNotEmpty()) {
                        itemFollow = results[0]
                }
        }

        @WorkerThread
        fun loadStock(id: Int) {
                val results = database.loadStocks(id)
                if (results.isNotEmpty()) {
                        stockDetail = results[0]
                }
        }

        @MainThread
        fun insert(itemFollow: ItemFollow) {
                io {
                        database.insertFollow(itemFollow)
                }
        }

        @MainThread
        fun update(itemFollow: ItemFollow) {
                io {
                        database.updateFollow(itemFollow)
                }
        }
}