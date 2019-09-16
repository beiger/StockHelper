package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.Summary

@Dao
interface SummaryDao {
        @Query("SELECT * FROM summary")
        fun loadItems(): List<Summary>

        @Query("SELECT * FROM summary")
        fun loadItemsLive(): LiveData<List<Summary>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: Summary)

        @Delete
        fun delete(item: Summary)

        @Update
        fun update(item: Summary)
}
