package com.bing.stockhelper.model

import android.content.Context

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bing.stockhelper.model.dao.*
import com.bing.stockhelper.model.entity.*

@Database(
        entities = [ItemFollow::class, OrderDetail::class, Summary::class, StockType::class, DayAttention::class],
        version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

        abstract fun itemFollowDao(): ItemFollowDao
        abstract fun orderDetailDao(): OrderDetailDao
        abstract fun summaryDao(): SummaryDao
        abstract fun stockTypeDao(): StockTypeDao
        abstract fun dayAttentionDao(): DayAttentionDao

        fun loadOrdersLive(): LiveData<List<OrderDetail>> = orderDetailDao().loadItemsLive()
        fun loadOrders(): List<OrderDetail> = orderDetailDao().loadItems()
        fun insertOrder(orderStatus: OrderDetail) = orderDetailDao().insert(orderStatus)
        fun updateOrder(orderStatus: OrderDetail) = orderDetailDao().update(orderStatus)
        fun deleteOrder(orderStatus: OrderDetail) = orderDetailDao().delete(orderStatus)

        fun loadFollowsLive(): LiveData<List<ItemFollow>> = itemFollowDao().loadItemsLive()
        fun loadFollows(): List<ItemFollow> = itemFollowDao().loadItems()
        fun insertFollow(itemFollow: ItemFollow) = itemFollowDao().insert(itemFollow)
        fun updateFollow(itemFollow: ItemFollow) = itemFollowDao().update(itemFollow)
        fun deleteFollow(itemFollow: ItemFollow) = itemFollowDao().delete(itemFollow)

        fun loadSummarysLive(): LiveData<List<Summary>> = summaryDao().loadItemsLive()
        fun loadSummarys(): List<Summary> = summaryDao().loadItems()
        fun insertSummary(summary: Summary) = summaryDao().insert(summary)
        fun updateSummary(summary: Summary) = summaryDao().update(summary)
        fun deleteSummary(summary: Summary) = summaryDao().delete(summary)

        fun loadStockTypesLive(): LiveData<List<StockType>> = stockTypeDao().loadItemsLive()
        fun loadStockTypes(): List<StockType> = stockTypeDao().loadItems()
        fun insertStockType(type: StockType) = stockTypeDao().insert(type)
        fun updateStockType(type: StockType) = stockTypeDao().update(type)
        fun deleteStockType(type: StockType) = stockTypeDao().delete(type)

        fun loadDayAttentionsLive(): LiveData<List<DayAttention>> = dayAttentionDao().loadItemsLive()
        fun loadDayAttentions(): List<DayAttention> = dayAttentionDao().loadItems()
        fun insertDayAttention(dayAttention: DayAttention) = dayAttentionDao().insert(dayAttention)
        fun updateDayAttention(dayAttention: DayAttention) = dayAttentionDao().update(dayAttention)
        fun deleteDayAttention(dayAttention: DayAttention) = dayAttentionDao().delete(dayAttention)

    companion object {
                @Volatile private var sInstance: AppDatabase? = null

                @VisibleForTesting
                val DATABASE_NAME = "test-db"

                fun getInstance(context: Context): AppDatabase {
                        return sInstance ?: synchronized(this) {
                                sInstance ?: buildDatabase(context).also { sInstance = it }
                        }
                }

                private fun buildDatabase(appContext: Context): AppDatabase {
                        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
                                .allowMainThreadQueries()
                                .build()
                }
        }
}
