package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.OrderDetail

@Dao
interface OrderDetailDao {
        @Query("SELECT * FROM orderDetail")
        fun loadItems(): List<OrderDetail>

        @Query("SELECT * FROM orderDetail")
        fun loadItemsLive(): LiveData<List<OrderDetail>>

        @Query("SELECT * FROM orderDetail where id=:id")
        fun loadItems(id: Int): List<OrderDetail>


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(item: OrderDetail)

        @Delete
        fun delete(item: OrderDetail)

        @Update
        fun update(item: OrderDetail)
}
