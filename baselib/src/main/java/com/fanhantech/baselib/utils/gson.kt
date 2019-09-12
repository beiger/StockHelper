package com.fanhantech.baselib.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GSON {
        val ins = Gson()
}

inline fun <reified T> genericType() = object: TypeToken<T>() {}.type
