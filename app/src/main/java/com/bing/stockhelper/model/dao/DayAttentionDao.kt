package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.DayAttention

@Dao
interface DayAttentionDao {
        @Query("SELECT * FROM dayAttention")
        fun loadItems(): List<DayAttention>

        @Query("SELECT * FROM summary")
        fun loadItemsLive(): LiveData<List<DayAttention>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: DayAttention)

        @Delete
        fun delete(item: DayAttention)

        @Query("DELETE FROM dayAttention")
        fun deleteAll()

        @Update
        fun update(item: DayAttention)
}
