package com.bing.stockhelper.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

const val COLLECTION_TYPE_IMAGE = 0
const val COLLECTION_TYPE_HTTP = 1
const val COLLECTION_TYPE_ARTICLE = 2

@Entity(tableName = "collectArticle")
data class CollectArticle(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var title: String,
        var content: String?,
        var imageUrl: String?,
        var httpUrl: String?,
        var type: Int
)