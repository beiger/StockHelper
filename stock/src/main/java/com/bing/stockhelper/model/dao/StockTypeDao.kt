package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.StockType

@Dao
interface StockTypeDao {
        @Query("SELECT * FROM stockTypes")
        fun loadItems(): List<StockType>

        @Query("SELECT * FROM stockTypes")
        fun loadItemsLive(): LiveData<List<StockType>>

        @Query("SELECT * FROM stockTypes where id=:id")
        fun loadItem(id: Int): StockType

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: StockType)

        @Delete
        fun delete(item: StockType)

        @Update
        fun update(item: StockType)
}
