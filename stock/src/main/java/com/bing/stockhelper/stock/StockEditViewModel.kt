package com.bing.stockhelper.stock

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.StockDetail
import com.fanhantech.baselib.app.io

class StockEditViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        private val stockDetail = MutableLiveData<StockDetail>()

        @WorkerThread
        fun load(id: Int): StockDetail? {
                val results = database.loadStocks(id)
                if (results.isEmpty()) {
                        return null
                }
                return results[0]
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