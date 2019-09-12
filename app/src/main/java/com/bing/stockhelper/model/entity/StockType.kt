package com.bing.stockhelper.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stockTypes")
class StockType(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var name: String
)