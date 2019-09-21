package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

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
}