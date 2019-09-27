package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.WorkerThread
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bing.stockhelper.model.AppDatabase

@Entity(tableName = "itemFollows")
data class ItemFollow(
        @PrimaryKey
        var stockId: Int,
        var comment: String?,
        // 0-10
        var focusDegree: Int
) : Parcelable {

        // 判断是否刷新界面
        fun isSameWith(item: ItemFollow): Boolean {
                return stockId == item.stockId &&
                        comment == item.comment &&
                        focusDegree == item.focusDegree
        }

        constructor(source: Parcel) : this(
                source.readInt(),
                source.readString(),
                source.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(stockId)
                writeString(comment)
                writeInt(focusDegree)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ItemFollow> = object : Parcelable.Creator<ItemFollow> {
                        override fun createFromParcel(source: Parcel): ItemFollow = ItemFollow(source)
                        override fun newArray(size: Int): Array<ItemFollow?> = arrayOfNulls(size)
                }

                fun instance(): ItemFollow = ItemFollow(
                        -1, null, 0
                )
        }

        @WorkerThread
        fun toInfo(dataBase: AppDatabase, firstTtags: List<StockTag>, secondTags: List<StockTag>): Info? {
                val stocks = dataBase.loadStocks(stockId)
                return if (stocks.isNotEmpty()) {
                        val stock = stocks[0]
                        Info(
                                stock.code,
                                stock.name,
                                stock.imgUrl,
                                stock.tagsStr(TAG_LEVEL_FIRST, firstTtags),
                                stock.tagsStr(TAG_LEVEL_SECOND, secondTags),
                                stock.description,
                                comment,
                                focusDegree
                        )
                } else {
                        null
                }
        }

        data class Info(
                var code: String,
                var name: String,
                var imgUrl: String?,
                var firstTags: String,
                var secondTags: String,
                var description: String?,
                var comment: String?,
                // 0-10
                var focusDegree: Int
        ) {
                fun isSameWith(item: Info): Boolean {
                        return code == item.code &&
                                name == item.name &&
                                imgUrl == item.imgUrl &&
                                firstTags == item.firstTags &&
                                secondTags == item.secondTags &&
                                description == item.description &&
                                comment == item.comment &&
                                focusDegree == item.focusDegree
                }
        }
}