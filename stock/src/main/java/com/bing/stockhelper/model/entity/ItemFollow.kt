package com.bing.stockhelper.model.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.WorkerThread
import androidx.room.*
import com.bing.stockhelper.model.AppDatabase
import java.lang.StringBuilder

@Entity(
        tableName = "itemFollows",
        foreignKeys = [ForeignKey(entity = StockDetail::class, parentColumns = ["id"], childColumns = ["stockId"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)],
        indices = [Index(value=["stockId"], unique = true)]
)
data class ItemFollow(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
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
                source.readInt(),
                source.readString(),
                source.readInt()
        )

        override fun describeContents() = 0

        override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
                writeInt(id)
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
                        0, -1, null, 0
                )
        }

        data class Info(
                var id: Int,
                var code: String,
                var name: String,
                var imgUrl: String?,
                var firstTags: MutableList<Int>,
                var secondTags: MutableList<Int>,
                var description: String?,
                var comment: String?,
                // 0-10
                var focusDegree: Int
        ) {
                fun isSameWith(item: Info): Boolean {
                        return id == id &&
                                code == item.code &&
                                name == item.name &&
                                imgUrl == item.imgUrl &&
                                firstTags.toString() == item.firstTags.toString() &&
                                secondTags.toString() == item.secondTags.toString() &&
                                description == item.description &&
                                comment == item.comment &&
                                focusDegree == item.focusDegree
                }

                fun tagsStr(level: Int, tags: List<StockTag>): String {
                        val levelTags = when (level) {
                                TAG_LEVEL_FIRST -> firstTags
                                TAG_LEVEL_SECOND -> secondTags
                                else -> null
                        } ?: return ""
                        if (tags.isEmpty()) {
                                levelTags.clear()
                                return ""
                        }
                        val builder = StringBuilder()
                        val iterator = levelTags.iterator()
                        while (iterator.hasNext()) {
                                val nextTag = iterator.next()
                                val tag = tags.firstOrNull { it.id == nextTag }
                                if (tag == null) {
                                        iterator.remove()
                                } else {
                                        builder.append(tag.name + TAG_SEPERATER)
                                }
                        }
                        return builder.dropLast(1).toString()
                }
        }
}