package com.bing.stockhelper.app

import com.blankj.utilcode.util.SPStaticUtils

object SettingSP {
        const val TAG_DRAWER_IMAGE = "drawer_image"

        fun getDrawerImageUrl(): String {
                return SPStaticUtils.getString(TAG_DRAWER_IMAGE)
        }

        fun setDrawerImageUrl(url: String) {
                return SPStaticUtils.put(TAG_DRAWER_IMAGE, url)
        }

        fun backup() {

        }

        fun recover() {

        }
}