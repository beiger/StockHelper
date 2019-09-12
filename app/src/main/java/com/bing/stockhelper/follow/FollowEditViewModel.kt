package com.bing.stockhelper.follow

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.ItemFollow
import com.fanhantech.baselib.app.io

class FollowEditViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

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