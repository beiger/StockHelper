package com.bing.stockhelper.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "concernedTag")
data class ConcernedTag(
    @PrimaryKey
    var stockTagId: Int
)