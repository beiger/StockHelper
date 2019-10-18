package com.bing.stockhelper.main.summary

import android.app.Application
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.fanhantech.baselib.app.io

class SummaryViewModel(application: Application) : AndroidViewModel(application) {

        private val database = AppDatabase.getInstance(application)

        val dayAttentions = database.loadDayAttentionsLive()
        var summaries = database.loadSummarysLive()

        @MainThread
        fun delete(summary: Summary) {
                io {
                        database.deleteSummary(summary)
                }
        }

        @WorkerThread
        fun deleteAllAttention() {
                database.deleteAllAttention()
        }

        @WorkerThread
        fun insert(item: DayAttention) {
                database.insertDayAttention(item)
        }
}