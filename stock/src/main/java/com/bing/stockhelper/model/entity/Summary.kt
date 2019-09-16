package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summary")
data class Summary(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var content: String,
        var lastEditTime: Long
) : Parcelable {
        constructor(source: Parcel) : this(
                source.readInt(),
                source.readString()!!,
                source.readLong()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(id)
                writeString(content)
                writeLong(lastEditTime)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<Summary> = object : Parcelable.Creator<Summary> {
                        override fun createFromParcel(source: Parcel): Summary = Summary(source)
                        override fun newArray(size: Int): Array<Summary?> = arrayOfNulls(size)
                }
        }
}