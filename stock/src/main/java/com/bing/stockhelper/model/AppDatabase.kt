package com.bing.stockhelper.model

import android.content.Context

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bing.stockhelper.model.dao.*
import com.bing.stockhelper.model.entity.*

@Database(
        entities = [StockDetail::class, StockTag::class, OrderDetail::class,
                ItemFollow::class, Summary::class, DayAttention::class,
                ConcernedTag::class, CollectArticle::class],
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
        abstract fun collectArticleDao(): CollectArticleDao

        fun loadStocksLive(): LiveData<List<StockDetail>> = stockDetailDao().loadItemsLive()
        fun loadStocks(): List<StockDetail> = stockDetailDao().loadItems()
        fun loadStocks(id: Int): List<StockDetail> = stockDetailDao().loadItems(id)
        fun loadStocksLive(id: Int): LiveData<List<StockDetail>> = stockDetailDao().loadItemsLive(id)
        fun insertStock(item: StockDetail) = stockDetailDao().insert(item)
        fun updateStock(item: StockDetail) = stockDetailDao().update(item)
        fun deleteStock(item: StockDetail) = stockDetailDao().delete(item)

        fun loadOrdersLive(): LiveData<List<OrderDetail>> = orderDetailDao().loadItemsLive()
        fun loadOrders(): List<OrderDetail> = orderDetailDao().loadItems()
        fun loadOrderInfosLive(): LiveData<List<OrderDetail.DetailInfo>> = orderDetailDao().loadItemInfosLive()
        fun loadOrders(id: Int): List<OrderDetail> = orderDetailDao().loadItems(id)
        fun insertOrder(item: OrderDetail) = orderDetailDao().insert(item)
        fun updateOrder(item: OrderDetail) = orderDetailDao().update(item)
        fun deleteOrder(item: OrderDetail) = orderDetailDao().delete(item)
        fun deleteOrder(id: Int) = orderDetailDao().delete(id)

        fun loadFollowsLive(): LiveData<List<ItemFollow>> = itemFollowDao().loadItemsLive()
        fun loadFollows(): List<ItemFollow> = itemFollowDao().loadItems()
        fun loadFollowInfosLive(): LiveData<List<ItemFollow.Info>> = itemFollowDao().loadItemInfosLive()
        fun loadFollows(id: Int): List<ItemFollow> = itemFollowDao().loadItems(id)
        fun insertFollow(item: ItemFollow) = itemFollowDao().insert(item)
        fun updateFollow(item: ItemFollow) = itemFollowDao().update(item)
        fun deleteFollow(item: ItemFollow) = itemFollowDao().delete(item)
        fun deleteFollow(id: Int) = itemFollowDao().delete(id)

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

        fun loadCollectArticlesLive(): LiveData<List<CollectArticle>> = collectArticleDao().loadItemsLive()
        fun loadCollectArticles(): List<CollectArticle> = collectArticleDao().loadItems()
        fun loadCollectArticles(id: Int): List<CollectArticle> = collectArticleDao().loadItems(id)
        fun loadCollectArticlesLive(id: Int): LiveData<List<CollectArticle>> = collectArticleDao().loadItemsLive(id)
        fun insertCollectArticle(item: CollectArticle) = collectArticleDao().insert(item)
        fun updateCollectArticle(item: CollectArticle) = collectArticleDao().update(item)
        fun deleteCollectArticle(item: CollectArticle) = collectArticleDao().delete(item)

    companion object {
                @Volatile private var sInstance: AppDatabase? = null

                @VisibleForTesting
                val DATABASE_NAME = "db-stock"

            var MIGRATION1_2: Migration = object : Migration(1, 2) {
                    override fun migrate(database: SupportSQLiteDatabase) {

                    }
            }

                fun getInstance(context: Context): AppDatabase {
                        return sInstance ?: synchronized(this) {
                                sInstance ?: buildDatabase(context).also { sInstance = it }
                        }
                }

                private fun buildDatabase(appContext: Context): AppDatabase {
                        return Room.databaseBuilder(appContext, AppDatabase::class.java, DATABASE_NAME)
//                                .addMigrations(MIGRATION1_2)
//                                .allowMainThreadQueries()
                                .build()
                }
        }
}
