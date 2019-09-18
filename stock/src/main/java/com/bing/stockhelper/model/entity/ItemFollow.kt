package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "itemFollows")
data class ItemFollow(
        @PrimaryKey
        var code: String,
        var name: String,
        var expectPrice: Float,
        var currentPrice: Float,
        var comment: String?,
        // 0-10
        var focusDegree: Int,
        var tags: String
) : Parcelable {

        fun isSameWith(item: ItemFollow): Boolean {
                return code == item.code &&
                        name == item.name &&
                        expectPrice == item.expectPrice &&
                        currentPrice == item.currentPrice &&
                        comment == item.comment &&
                        focusDegree == item.focusDegree &&
                        tags == item.tags
        }

        constructor(source: Parcel) : this(
                source.readString()!!,
                source.readString()!!,
                source.readFloat(),
                source.readFloat(),
                source.readString(),
                source.readInt(),
                source.readString()!!
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(code)
                writeString(name)
                writeFloat(expectPrice)
                writeFloat(currentPrice)
                writeString(comment)
                writeInt(focusDegree)
                writeString(tags)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ItemFollow> = object : Parcelable.Creator<ItemFollow> {
                        override fun createFromParcel(source: Parcel): ItemFollow = ItemFollow(source)
                        override fun newArray(size: Int): Array<ItemFollow?> = arrayOfNulls(size)
                }

                fun instance(): ItemFollow = ItemFollow(
                        "", "", 0f, 0f, null, 0, ""
                )
        }
}