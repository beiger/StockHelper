package com.bing.stockhelper.utils

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.WorkerThread
import com.fanhantech.baselib.utils.BitmapUtil
import com.vansuita.gaussianblur.GaussianBlur
import java.io.File
import java.io.FileOutputStream

@WorkerThread
fun gaussianBlur(context: Context, src: String, des: String) {
        if (File(des).exists()) {
                return
        }
        val bitmap = BitmapUtil.decodeSampledBitmapFromFile(src, 500, 500)
        val gsBitmap = GaussianBlur.with(context).size(200f).radius(20).render(bitmap)
        try {
                val fileOutputStream = FileOutputStream(des)
                gsBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)//设置PNG的话，透明区域不会变成黑色

                fileOutputStream.close()
        } catch (e: Exception) {
                e.printStackTrace()
        }
}

