package com.fanhantech.baselib.model.datawrapper

/**
 * 数据请求状态
 */
enum class Status {
        /**
         * 正在加载中
         */
        LOADING,

        /**
         * 加载成功
         */
        SUCCESS,

        /**
         * code不为0，或者网络错误
         */
        ERROR,

        /**
         * 内容为空
         */
        EMPTY
}
