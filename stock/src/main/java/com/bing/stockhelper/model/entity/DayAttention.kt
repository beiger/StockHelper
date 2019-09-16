package com.bing.stockhelper.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dayAttention")
data class DayAttention(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var content: String
)