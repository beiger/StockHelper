package com.bing.stockhelper.main

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import com.bing.stockhelper.R
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.*
import com.bing.stockhelper.utils.Constant
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.FileUtils
import com.blankj.utilcode.util.ToastUtils
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.utils.GSON
import com.fanhantech.baselib.utils.genericType
import java.io.File

class MainViewModel(application: Application): AndroidViewModel(application) {
        private val database = AppDatabase.getInstance(application)

        @MainThread
        fun insert(orderStatus: OrderDetail) {
                io {
                        database.insertOrder(orderStatus)
                }
        }

        @MainThread
        fun backup() {
                io {
                        val tags = database.loadStockTags()
                        val stocks = database.loadStocks()
                        val orders = database.loadOrders()
                        val follows = database.loadFollows()
                        val summarys = database.loadSummarys()
                        val dayAttention = database.loadDayAttentions()
                        val concernedTags = database.loadConcernedTags()
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "tags.json", GSON.ins.toJson(tags))
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "stocks.json", GSON.ins.toJson(stocks))
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "orders.json", GSON.ins.toJson(orders))
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "follows.json", GSON.ins.toJson(follows))
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "summarys.json", GSON.ins.toJson(summarys))
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "dayAttention.json", GSON.ins.toJson(dayAttention))
                        FileIOUtils.writeFileFromString(Constant.BACKUP_FILE_DIR + "concernedTags.json", GSON.ins.toJson(concernedTags))
                        ToastUtils.showShort(R.string.backup_finished)
                }
        }

        @MainThread
        fun recoverFromFile(dir: String) {
                io {
                        try {
                                val tags = GSON.ins.fromJson<List<StockTag>>(File("$dir/tags.json").readText(), genericType<List<StockTag>>())
                                val stocks = GSON.ins.fromJson<List<StockDetail>>(File("$dir/stocks.json").readText(), genericType<List<StockDetail>>())
                                val orders = GSON.ins.fromJson<List<OrderDetail>>(File("$dir/orders.json").readText(), genericType<List<OrderDetail>>())
                                val follows = GSON.ins.fromJson<List<ItemFollow>>(File("$dir/follows.json").readText(), genericType<List<ItemFollow>>())
                                val summarys = GSON.ins.fromJson<List<Summary>>(File("$dir/summarys.json").readText(), genericType<List<Summary>>())
                                val dayAttention = GSON.ins.fromJson<List<DayAttention>>(File("$dir/dayAttention.json").readText(), genericType<List<DayAttention>>())
                                val concernedTags = GSON.ins.fromJson<List<ConcernedTag>>(File("$dir/concernedTags.json").readText(), genericType<List<ConcernedTag>>())

                                database.insertStockTags(tags)
                                database.insertStocks(stocks)
                                database.insertOrders(orders)
                                database.insertFollows(follows)
                                database.insertSummarys(summarys)
                                database.insertDayAttentions(dayAttention)
                                database.insertConcernedTags(concernedTags)
                                ToastUtils.showShort(R.string.recover_finished)
                        } catch (e: Exception) {
                                ToastUtils.showShort(e.message)
                        }
                }
        }
}