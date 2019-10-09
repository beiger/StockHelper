package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.StockDetail
import com.bing.stockhelper.model.entity.StockTag

@Dao
interface StockDetailDao {
        @Query("SELECT * FROM stockDetail")
        fun loadItems(): List<StockDetail>

        @Query("SELECT * FROM stockDetail")
        fun loadItemsLive(): LiveData<List<StockDetail>>

        @Query("SELECT * FROM stockDetail where id=:id")
        fun loadItems(id: Int): List<StockDetail>

        @Query("SELECT * FROM stockDetail where id=:id")
        fun loadItemsLive(id: Int): LiveData<List<StockDetail>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: StockDetail)

        @Delete
        fun delete(item: StockDetail)

        @Update
        fun update(item: StockDetail)
}
