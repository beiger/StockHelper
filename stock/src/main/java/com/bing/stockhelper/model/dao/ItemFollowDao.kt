package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.ItemFollow
import com.bing.stockhelper.model.entity.OrderDetail

@Dao
interface ItemFollowDao {
        @Query("SELECT * FROM itemFollows")
        fun loadItems(): List<ItemFollow>

        @Query("SELECT itemFollows.id, code, name, imgUrl, firstTags," +
                " secondTags, description, comment, focusDegree FROM itemFollows left join stockDetail where stockId=stockDetail.id")
        fun loadItemInfosLive(): LiveData<List<ItemFollow.Info>>

        @Query("SELECT * FROM itemFollows")
        fun loadItemsLive(): LiveData<List<ItemFollow>>
        
        @Query("SELECT * FROM itemFollows where stockId=:id")
        fun loadItems(id: Int): List<ItemFollow>
        
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: ItemFollow)

        @Delete
        fun delete(item: ItemFollow)

        @Query("DELETE FROM itemFollows where id=:id")
        fun delete(id: Int)

        @Update
        fun update(item: ItemFollow)
}
