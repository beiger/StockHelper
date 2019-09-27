package com.bing.stockhelper.model

import android.content.Context

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bing.stockhelper.model.dao.*
import com.bing.stockhelper.model.entity.*

@Database(
        entities = [StockDetail::class, StockTag::class, OrderDetail::class,
                ItemFollow::class, Summary::class, DayAttention::class,
                ConcernedTag::class],
        version = 1,
        exportSchema = false)
@TypeConverters(IntListConverter::class)
abstract class AppDatabase : RoomDatabase() {

        abstract fun stockDetailDao(): StockDetailDao
        abstract fun stockTagDao(): StockTagDao
        abstract fun orderDetailDao(): OrderDetailDao
        abstract fun itemFollowDao(): ItemFollowDao
        abstract fun summaryDao(): SummaryDao
        abstract fun dayAttentionDao(): DayAttentionDao
        abstract fun concernedTagDao(): ConcernedTagDao

        fun loadStocksLive(): LiveData<List<StockDetail>> = stockDetailDao().loadItemsLive()
        fun loadStocks(): List<StockDetail> = stockDetailDao().loadItems()
        fun loadStocks(id: Int): List<StockDetail> = stockDetailDao().loadItems(id)
        fun insertStock(item: StockDetail) = stockDetailDao().insert(item)
        fun updateStock(item: StockDetail) = stockDetailDao().update(item)
        fun deleteStock(item: StockDetail) = stockDetailDao().delete(item)

        fun loadOrdersLive(): LiveData<List<OrderDetail>> = orderDetailDao().loadItemsLive()
        fun loadOrders(): List<OrderDetail> = orderDetailDao().loadItems()
        fun loadOrders(id: Int): List<OrderDetail> = orderDetailDao().loadItems(id)
        fun insertOrder(item: OrderDetail) = orderDetailDao().insert(item)
        fun updateOrder(item: OrderDetail) = orderDetailDao().update(item)
        fun deleteOrder(item: OrderDetail) = orderDetailDao().delete(item)

        fun loadFollowsLive(): LiveData<List<ItemFollow>> = itemFollowDao().loadItemsLive()
        fun loadFollows(): List<ItemFollow> = itemFollowDao().loadItems()
        fun loadFollows(id: Int): List<ItemFollow> = itemFollowDao().loadItems(id)
        fun insertFollow(item: ItemFollow) = itemFollowDao().insert(item)
        fun updateFollow(item: ItemFollow) = itemFollowDao().update(item)
        fun deleteFollow(item: ItemFollow) = itemFollowDao().delete(item)

        fun loadSummarysLive(): LiveData<List<Summary>> = summaryDao().loadItemsLive()
        fun loadSummarys(): List<Summary> = summaryDao().loadItems()
        fun insertSummary(item: Summary) = summaryDao().insert(item)
        fun updateSummary(item: Summary) = summaryDao().update(item)
        fun deleteSummary(item: Summary) = summaryDao().delete(item)

        fun loadStockTagsLive(): LiveData<List<StockTag>> = stockTagDao().loadItemsLive()
        fun loadStockTags(): List<StockTag> = stockTagDao().loadItems()
        fun insertStockTag(item: StockTag) = stockTagDao().insert(item)
        fun updateStockTag(item: StockTag) = stockTagDao().update(item)
        fun deleteStockTag(item: StockTag) = stockTagDao().delete(item)

        fun loadDayAttentionsLive(): LiveData<List<DayAttention>> = dayAttentionDao().loadItemsLive()
        fun loadDayAttentions(): List<DayAttention> = dayAttentionDao().loadItems()
        fun insertDayAttention(item: DayAttention) = dayAttentionDao().insert(item)
        fun updateDayAttention(item: DayAttention) = dayAttentionDao().update(item)
        fun deleteDayAttention(item: DayAttention) = dayAttentionDao().delete(item)
        fun deleteAllAttention() = dayAttentionDao().deleteAll()

        fun loadConcernedTagsLive(): LiveData<List<ConcernedTag>> = concernedTagDao().loadItemsLive()
        fun loadConcernedTags(): List<ConcernedTag> = concernedTagDao().loadItems()
        fun insertConcernedTag(item: ConcernedTag) = concernedTagDao().insert(item)
        fun updateConcernedag(item: ConcernedTag) = concernedTagDao().update(item)
        fun deleteConcernedTag(item: ConcernedTag) = concernedTagDao().delete(item)

    companion object {
                @Volatile private var sInstance: AppDatabase? = null

                @VisibleForTesting
                val DATABASE_NAME = "db-stock"

                fun getInstance(context: Context): AppDatabase {
                        return sInstance ?: synchronized(this) {
                                sInstance ?: buildDatabase(context).also { sInstance = it }
                        }
                }

                private fun buildDatabase(appContext: Context): AppDatabase {
                        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
//                                .allowMainThreadQueries()
                                .build()
                }
        }
}
