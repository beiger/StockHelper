package com.bing.stockhelper.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bing.stockhelper.model.entity.OrderDetail

@Dao
interface OrderDetailDao {
        @Query("SELECT * FROM orderDetail")
        fun loadItems(): List<OrderDetail>

        @Query("SELECT orderDetail.id, orderNum, code, name, imgUrl, firstTags," +
                " secondTags, description, buyTime, buyPrice, expectPrice, currentPrice, sellPrice," +
                " buyNum, isHold, comment FROM orderDetail left join stockDetail where stockId=stockDetail.id")
        fun loadItemInfosLive(): LiveData<List<OrderDetail.DetailInfo>>

        @Query("SELECT * FROM orderDetail")
        fun loadItemsLive(): LiveData<List<OrderDetail>>

        @Query("SELECT * FROM orderDetail where id=:id")
        fun loadItems(id: Int): List<OrderDetail>


        @Insert(onConflict = OnConflictStrategy.IGNORE)
        fun insert(item: OrderDetail)

        @Delete
        fun delete(item: OrderDetail)

        @Query("DELETE FROM orderDetail where id=:id")
        fun delete(id: Int)

        @Update
        fun update(item: OrderDetail)
}
