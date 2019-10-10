package com.bing.stockhelper.utils

import com.fanhantech.baselib.utils.BaseConstant
import java.io.File

object Constant {
    const val TAG_POSITION = "position"
    const val TAG_ORDER_DETAIL = "order_detail"
    const val TAG_ORDER_DETAIL_ID = "order_detail_id"
    const val TAG_STOCK_DETAIL = "stock_detail"
    const val TAG_STOCK_ID= "stock_id"
    const val TAG_COLLECT_ARTICLE_ID= "collect_article_id"
    const val TAG_SUMMARY = "summary"
    const val TAG_ITEM_FOLLOW = "item_follow"
    const val TAG_ITEM_FOLLOW_ID = "item_follow_id"
    const val TAG_MAIN_COLOR = "main_color"

    val COLLECT_File_DIR = BaseConstant.Out_File_DIR + "stock/collect" + File.separator
}