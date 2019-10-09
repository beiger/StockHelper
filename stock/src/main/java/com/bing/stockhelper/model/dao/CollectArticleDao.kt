package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.CollectArticle

@Dao
interface CollectArticleDao {
        @Query("SELECT * FROM collectArticle")
        fun loadItems(): List<CollectArticle>

        @Query("SELECT * FROM collectArticle")
        fun loadItemsLive(): LiveData<List<CollectArticle>>

        @Query("SELECT * FROM collectArticle where id=:id")
        fun loadItems(id: Int): List<CollectArticle>

        @Query("SELECT * FROM collectArticle where id=:id")
        fun loadItemsLive(id: Int): LiveData<List<CollectArticle>>

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: CollectArticle)

        @Delete
        fun delete(item: CollectArticle)

        @Update
        fun update(item: CollectArticle)
}
