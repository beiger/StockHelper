package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.ItemFollow

@Dao
interface ItemFollowDao {
        @Query("SELECT * FROM itemFollows")
        fun loadItems(): List<ItemFollow>

        @Query("SELECT * FROM itemFollows")
        fun loadItemsLive(): LiveData<List<ItemFollow>>
        
        @Query("SELECT * FROM itemFollows where stockId=:id")
        fun loadItems(id: Int): List<ItemFollow>
        
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: ItemFollow)

        @Delete
        fun delete(item: ItemFollow)

        @Update
        fun update(item: ItemFollow)
}
