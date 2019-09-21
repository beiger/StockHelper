package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.StockTag

@Dao
interface StockTagDao {
        @Query("SELECT * FROM stockTag")
        fun loadItems(): List<StockTag>

        @Query("SELECT * FROM stockTag")
        fun loadItemsLive(): LiveData<List<StockTag>>

        @Query("SELECT * FROM stockTag where id=:id")
        fun loadItems(id: Int): List<StockTag>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: StockTag)

        @Delete
        fun delete(item: StockTag)

        @Update
        fun update(item: StockTag)
}
