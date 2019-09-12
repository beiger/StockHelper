package com.bing.stockhelper.main.follow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.ItemFollow

class FollowViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getInstance(application)

    val follows = database.loadFollowsLive()

    fun delete(item: ItemFollow) {
        database.deleteFollow(item)
    }
}