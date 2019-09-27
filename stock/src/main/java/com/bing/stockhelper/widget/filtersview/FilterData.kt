package com.bing.stockhelper.widget.filtersview

const val FILTER_TYPE_SINGLE = 0
const val FILTER_TYPE_MUTIPLE = 1
const val FILTER_TYPE_INPUT = 2

class SingleData(val title: String, val choices: List<String>)

class MulData(val title: String, val choices: List<String>)

class InputData(val title: String)