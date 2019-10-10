package com.bing.stockhelper.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.lang.StringBuilder

const val COLLECTION_TYPE_IMAGE = 0
const val COLLECTION_TYPE_HTTP = 1
const val COLLECTION_TYPE_ARTICLE = 2

@Entity(tableName = "collectArticle")
data class CollectArticle(
        @PrimaryKey(autoGenerate = true)
        var id: Int,
        var title: String,
        var type: Int,
        var content: String?,
        var imageUrl: String?,
        var httpUrl: String?,
        var firstTags: MutableList<Int>,
        var secondTags: MutableList<Int>
) {
        // 刷新界面用的
        fun isSameWith(item: CollectArticle): Boolean {
                return id == item.id &&
                        title == item.title &&
                        type == item.type &&
                        content== item.content &&
                        imageUrl== item.imageUrl &&
                        httpUrl== item.httpUrl &&
                        firstTags.toString() == item.firstTags.toString() &&
                        secondTags.toString() == item.secondTags.toString()
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