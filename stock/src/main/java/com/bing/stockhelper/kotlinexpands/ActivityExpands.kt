package com.bing.stockhelper.kotlinexpands

import android.app.Activity
import com.blankj.utilcode.util.SizeUtils
import com.r0adkll.slidr.Slidr
import com.r0adkll.slidr.model.SlidrConfig
import com.r0adkll.slidr.model.SlidrPosition

fun Activity.initSlideBack() {
        Slidr.attach(this, SlidrConfig.Builder().position(SlidrPosition.LEFT).touchSize(SizeUtils.dp2px(32f).toFloat()).build())
}