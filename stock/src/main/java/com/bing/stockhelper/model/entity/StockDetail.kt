package com.bing.stockhelper.model.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.fanhantech.baselib.utils.GSON
import com.fanhantech.baselib.utils.genericType
import java.lang.StringBuilder

@Entity(
        tableName = "stockDetail",
        indices = [Index(value = ["code"], unique = true), Index(value = ["name"], unique = true)]
)
data class StockDetail(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var code: String,
        var name: String,
        var imgUrl: String?,
        var firstTags: MutableList<Int>,
        var secondTags: MutableList<Int>,
        var description: String?
) {
        // 刷新界面用的
        fun isSameWith(item: StockDetail): Boolean {
                return code == item.code &&
                        name == item.name &&
                        imgUrl == item.imgUrl &&
                        firstTags.toString() == item.firstTags.toString() &&
                        secondTags.toString() == item.secondTags.toString() &&
                        description == item.description
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
                return  builder.dropLast(1).toString()
        }
}

const val TAG_LEVEL_FIRST = 0
const val TAG_LEVEL_SECOND = 1

const val TAG_SEPERATER = "，"

@Entity(
        tableName = "stockTag",
        indices = [Index(value = ["name"], unique = true)]
)
class StockTag(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var name: String,
        var level: Int
) {
        fun isSameWith(item: StockTag): Boolean {
                return id == item.id &&
                        name == item.name &&
                        level == item.level
        }

        fun copy(): StockTag {
                return StockTag(id, name, level)
        }
}

class IntListConverter {
        @TypeConverter
        fun  revertIntList(jsonstr: String): MutableList<Int> {
                val type = genericType<MutableList<Int>>()
                return GSON.ins.fromJson(jsonstr, type)
        }

        @TypeConverter
        fun  convertIntList(info: MutableList<Int>): String {
                return GSON.ins.toJson(info)
        }
}