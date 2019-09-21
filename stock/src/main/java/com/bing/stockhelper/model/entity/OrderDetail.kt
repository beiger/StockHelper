package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "orderDetail")
data class OrderDetail(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var orderNum: Long,
        var stockId: Int,
        var buyTime: Long,
        var buyPrice: Float,
        var expectPrice: Float,
        var currentPrice: Float,
        var sellPrice: Float?,
        var buyNum: Int,
        var isHold: Boolean = true,
        var comment: String?
) : Parcelable {
        fun holdDays(): Int {
                val buyDate = Calendar.getInstance()
                buyDate.timeInMillis = buyTime
                buyDate.set(
                        buyDate.get(Calendar.YEAR),
                        buyDate.get(Calendar.MONTH),
                        buyDate.get(Calendar.DAY_OF_MONTH),
                        0, 0, 0
                )
                val now = Calendar.getInstance()
                now.timeInMillis = System.currentTimeMillis()
                now.set(
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH),
                        23, 59, 59
                )
                return ((now.timeInMillis - buyDate.timeInMillis) / (1000 * 60 * 60 * 24) + 1).toInt()
        }

        fun isSameWith(item: OrderDetail): Boolean {
                return orderNum == item.orderNum &&
                        stockId == item.stockId &&
                        buyTime == item.buyTime &&
                        buyPrice == item.buyPrice &&
                        expectPrice == item.expectPrice &&
                        currentPrice == item.currentPrice &&
                        sellPrice == item.sellPrice &&
                        buyNum == item.buyNum &&
                        isHold == item.isHold &&
                        comment == item.comment
        }

        constructor(source: Parcel) : this(
                source.readInt(),
                source.readLong(),
                source.readInt(),
                source.readLong(),
                source.readFloat(),
                source.readFloat(),
                source.readFloat(),
                source.readValue(Float::class.java.classLoader) as Float?,
                source.readInt(),
                1 == source.readInt(),
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(id)
                writeLong(orderNum)
                writeInt(stockId)
                writeLong(buyTime)
                writeFloat(buyPrice)
                writeFloat(expectPrice)
                writeFloat(currentPrice)
                writeValue(sellPrice)
                writeInt(buyNum)
                writeInt((if (isHold) 1 else 0))
                writeString(comment)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<OrderDetail> = object : Parcelable.Creator<OrderDetail> {
                        override fun createFromParcel(source: Parcel): OrderDetail = OrderDetail(source)
                        override fun newArray(size: Int): Array<OrderDetail?> = arrayOfNulls(size)
                }

                fun instance(): OrderDetail = OrderDetail(
                        0, 0L, -1, 0L, 0f, 0f, 0f, null, 0, true, null
                )
        }
}