package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orderDetail")
data class OrderDetail(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var orderNum: Long,
        var code: String,
        var name: String,
        var imgUrl: String?,
        var buyTime: Long,
        var buyPrice: Float,
        var expectPrice: Float,
        var currentPrice: Float,
        var sellPrice: Float?,
        var buyNum: Int,
        var todayOp: String?,
        var tomorrowOp: String?,
        var isHold: Boolean = true,
        var comment: String?,
        var tags: String
) : Parcelable {
        fun holdDays(): Int {
                //TODO
                return 1
        }

        fun isSameWith(item: OrderDetail): Boolean {
                return orderNum == item.orderNum &&
                        code == item.code &&
                        name == item.name &&
                        imgUrl == item.imgUrl &&
                        buyTime == item.buyTime &&
                        buyPrice == item.buyPrice &&
                        expectPrice == item.expectPrice &&
                        currentPrice == item.currentPrice &&
                        sellPrice == item.sellPrice &&
                        buyNum == item.buyNum &&
                        todayOp == item.todayOp &&
                        tomorrowOp == item.tomorrowOp &&
                        isHold == item.isHold &&
                        comment == item.comment &&
                        tags == item.tags
        }

        constructor(source: Parcel) : this(
                source.readInt(),
                source.readLong(),
                source.readString()!!,
                source.readString()!!,
                source.readString(),
                source.readLong(),
                source.readFloat(),
                source.readFloat(),
                source.readFloat(),
                source.readValue(Float::class.java.classLoader) as Float?,
                source.readInt(),
                source.readString(),
                source.readString(),
                1 == source.readInt(),
                source.readString(),
                source.readString()!!
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(id)
                writeLong(orderNum)
                writeString(code)
                writeString(name)
                writeString(imgUrl)
                writeLong(buyTime)
                writeFloat(buyPrice)
                writeFloat(expectPrice)
                writeFloat(currentPrice)
                writeValue(sellPrice)
                writeInt(buyNum)
                writeString(todayOp)
                writeString(tomorrowOp)
                writeInt((if (isHold) 1 else 0))
                writeString(comment)
                writeString(tags)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<OrderDetail> = object : Parcelable.Creator<OrderDetail> {
                        override fun createFromParcel(source: Parcel): OrderDetail = OrderDetail(source)
                        override fun newArray(size: Int): Array<OrderDetail?> = arrayOfNulls(size)
                }

                fun instance(): OrderDetail = OrderDetail(
                        0, 0L, "", "", null, 0L, 0f, 0f, 0f, null, 0, null, null, true, null, ""
                )
        }
}