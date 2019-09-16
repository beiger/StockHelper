package com.bing.stockhelper.summary

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.Summary
import com.fanhantech.baselib.app.io

class SummaryViewModel(application: Application): AndroidViewModel(application) {
        val database = AppDatabase.getInstance(application)

        var summaries = database.loadSummarysLive()

        @MainThread
        fun delete(summary: Summary) {
                io {
                        database.deleteSummary(summary)
                }
        }
}