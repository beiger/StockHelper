package com.bing.stockhelper.widget.filtersview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.EditText
import com.bing.stockhelper.R
import com.fanhantech.baselib.kotlinExpands.afterTextChanged

class InputFilterView: FrameLayout {
        var result: String? = null

        constructor(context: Context, data: InputData): this(context) {
                init(context, data)
        }
        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        private fun init(context: Context, data: InputData) {
                val rootView = View.inflate(context, R.layout.view_input_filter, this)
                val title = rootView.findViewById<TextView>(R.id.title)
                title.text = data.title
                val content = rootView.findViewById<EditText>(R.id.content)
                content.afterTextChanged {
                        result = it.toString().trim()
                }
        }
}