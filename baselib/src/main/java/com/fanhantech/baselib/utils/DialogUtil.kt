package com.fanhantech.baselib.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.fanhantech.baselib.R

/**
 * 用于全屏禁止点击事件
 */
fun createTranslucentDialog(context: Context): AlertDialog {
        return AlertDialog.Builder(context, R.style.TransparentDialog).setCancelable(false).create()
}
