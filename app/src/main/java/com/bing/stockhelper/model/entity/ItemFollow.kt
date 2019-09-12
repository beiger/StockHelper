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
        var comment: String,
        // 0-10
        var focusDegree: Int,
        var typeId: Int
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readString()!!,
                source.readString()!!,
                source.readFloat(),
                source.readFloat(),
                source.readString()!!,
                source.readInt(),
                source.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeString(code)
                writeString(name)
                writeFloat(expectPrice)
                writeFloat(currentPrice)
                writeString(comment)
                writeInt(focusDegree)
                writeInt(typeId)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<ItemFollow> = object : Parcelable.Creator<ItemFollow> {
                        override fun createFromParcel(source: Parcel): ItemFollow = ItemFollow(source)
                        override fun newArray(size: Int): Array<ItemFollow?> = arrayOfNulls(size)
                }

                fun instance(): ItemFollow = ItemFollow(
                        "", "", 0f, 0f, "", 0, -1
                )
        }
}