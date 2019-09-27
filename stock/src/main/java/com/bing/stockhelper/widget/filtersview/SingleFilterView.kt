package com.bing.stockhelper.widget.filtersview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.view.ViewGroup
import com.google.android.flexbox.FlexboxLayout
import androidx.core.view.ViewCompat
import android.view.Gravity
import com.bing.stockhelper.R
import com.blankj.utilcode.util.SizeUtils

class SingleFilterView: FrameLayout {
        val tvList = mutableListOf<TextView>()
        var selectedPosition: Int? = null

        constructor(context: Context, data: SingleData): this(context) {
                init(context, data)
        }
        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

        private fun init(context: Context, data: SingleData) {
                val rootView = View.inflate(context, R.layout.view_single_filter, this)
                val title = rootView.findViewById<TextView>(R.id.title)
                title.text = data.title
                val container = rootView.findViewById<FlexboxLayout>(R.id.content)
                for ((i, item) in data.choices.withIndex()) {
                        val textView = createItem(item, i)
                        container.addView(textView)
                        tvList.add(textView)
                }
        }

        private fun createItem(name: String, position: Int): TextView {
                val textView = TextView(context)
                textView.gravity = Gravity.CENTER
                textView.text = name
                textView.textSize = 12f
                textView.setTextColor(resources.getColor(R.color.color_aaaaaa))
                textView.setBackgroundResource(R.drawable.bg_filterview_single)
                textView.setOnClickListener {
                        setSelectedPosition(position)
                }
                val padding = SizeUtils.dp2px(4f)
                val paddingLeftAndRight = SizeUtils.dp2px(8f)
                ViewCompat.setPaddingRelative(textView, paddingLeftAndRight, padding, paddingLeftAndRight, padding)
                val layoutParams = FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT)
                val margin = SizeUtils.dp2px(6f)
                val marginTop = SizeUtils.dp2px(16f)
                layoutParams.setMargins(margin, marginTop, margin, 0)
                textView.layoutParams = layoutParams
                return textView
        }

        private fun setSelectedPosition(position: Int) {
                if (selectedPosition == position) {
                        tvList[position].isSelected = false
                        tvList[position].setTextColor(resources.getColor(R.color.color_aaaaaa))
                        selectedPosition = null
                        return
                }
                selectedPosition?.let {
                        tvList[it].isSelected = false
                        tvList[it].setTextColor(resources.getColor(R.color.color_aaaaaa))
                }
                tvList[position].isSelected = true
                tvList[position].setTextColor(Color.WHITE)
                selectedPosition = position
        }
}