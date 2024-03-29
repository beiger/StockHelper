package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.CollectArticle
import com.bing.stockhelper.model.entity.ConcernedTag
import com.bing.stockhelper.model.entity.Summary

@Dao
interface ConcernedTagDao {
        @Query("SELECT * FROM concernedTag")
        fun loadItems(): List<ConcernedTag>

        @Query("SELECT * FROM concernedTag")
        fun loadItemsLive(): LiveData<List<ConcernedTag>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insert(item: ConcernedTag)

        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insert(items: List<ConcernedTag>)

        @Delete
        fun delete(item: ConcernedTag)

        @Update
        fun update(item: ConcernedTag)
}
