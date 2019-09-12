package com.fanhantech.baselib.kotlinExpands

import android.view.View

fun View.OnClickListener.addClickableViews(vararg views: View) {
        for (view in views) {
                view.setOnClickListener(this)
        }
}