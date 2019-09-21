package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey


const val HOLD_STATE_NOT_BUY = -1
const val HOLD_STATE_HOLD = 0
const val HOLD_STATE_SELL = 1

const val FOLLOW_STATE_FOLLOW = 0
const val FOLLOW_STATE_NOT = 1


@Entity(tableName = "stockDetail")
data class StockDetail(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var code: String,
        var name: String,
        var imgUrl: String?,
        var firstTags: List<Int>,
        var secondTags: List<Int>,
        var description: String?
) : Parcelable {
        // 刷新界面用的
        fun isSameWith(item: StockDetail): Boolean {
                return code == item.code &&
                        name == item.name &&
                        imgUrl == item.imgUrl &&
                        firstTags.toString() == item.firstTags.toString() &&
                        secondTags.toString() == item.secondTags.toString() &&
                        description == item.description
        }

        constructor(source: Parcel) : this(
                source.readInt(),
                source.readString()!!,
                source.readString()!!,
                source.readString(),
                ArrayList<Int>().apply { source.readList(this as List<*>, Int::class.java.classLoader) },
                ArrayList<Int>().apply { source.readList(this as List<*>, Int::class.java.classLoader) },
                source.readString()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(id)
                writeString(code)
                writeString(name)
                writeString(imgUrl)
                writeList(firstTags)
                writeList(secondTags)
                writeString(description)
        }

        companion object {
                @JvmField
                val CREATOR: Parcelable.Creator<StockDetail> = object : Parcelable.Creator<StockDetail> {
                        override fun createFromParcel(source: Parcel): StockDetail = StockDetail(source)
                        override fun newArray(size: Int): Array<StockDetail?> = arrayOfNulls(size)
                }
        }
}

const val LEVEL_FIRST = 0
const val LEVEL_SECOND = 1

@Entity(tableName = "stockTag")
class StockTag(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var name: String,
        var level: Int
)