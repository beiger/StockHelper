package com.fanhantech.fanhandata.utils.kotlinExpands

fun <T1, T2> ifNotNull(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> Unit) {
        if (value1 != null && value2 != null) {
                bothNotNull(value1, value2)
        }
}

fun <T1, T2> ifNotNullElse(value1: T1?, value2: T2?, bothNotNull: (T1, T2) -> Unit, ifNull: (T1?, T2?) -> Unit) {
        if (value1 != null && value2 != null) {
                bothNotNull(value1, value2)
        } else {
                ifNull(value1, value2)
        }
}