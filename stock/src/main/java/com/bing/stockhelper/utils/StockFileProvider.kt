package com.bing.stockhelper.utils

import android.net.Uri
import androidx.core.content.FileProvider
import com.fanhantech.baselib.app.BaseApplication
import java.io.File

class StockFileProvider : FileProvider() {
        companion object {
                fun getUriFromFile(file: File): Uri {
                        return getUriForFile(BaseApplication.context, "com.bing.stockHelper.fileprovider", file)
                }
        }
}