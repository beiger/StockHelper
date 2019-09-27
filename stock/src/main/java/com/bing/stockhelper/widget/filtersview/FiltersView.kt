package com.bing.stockhelper.widget.filtersview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.LinearLayout
import com.bing.stockhelper.R

class FiltersView: FrameLayout {

        private lateinit var content: LinearLayout
        private val children = mutableListOf<View>()
        var onOk: ((List<Any>, List<Any?>) -> Unit)? = null
        var onCancel: ((List<Any>, List<Any?>) -> Unit)? = null

        constructor(context: Context, datas: List<Any>, _onOk: ((List<Any>, List<Any?>) -> Unit), _onCancel: ((List<Any>, List<Any?>) -> Unit)): this(context) {
                onOk = _onOk
                onCancel = _onCancel
                init(context, datas)
        }
        constructor(context: Context) : this(context, null)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
                init(context)
        }

        private fun init(context: Context) {
                val rootView = View.inflate(context, R.layout.view_filter, this)
                content = rootView.findViewById(R.id.content)
        }

        private fun init(context: Context, datas: List<Any>) {
                val rootView = View.inflate(context, R.layout.view_filter, this)
                content = rootView.findViewById(R.id.content)
                datas.forEach {
                        val view = when (it) {
                                is SingleData -> SingleFilterView(context, it)
                                is MulData -> MultipleFilterView(context, it)
                                is InputData -> InputFilterView(context, it)
                                else -> null
                        }
                        view?.let {
                                content.addView(view)
                                children.add(view)
                        }
                }
                val btOK = rootView.findViewById<TextView>(R.id.ok)
                btOK.setOnClickListener {
                        onOk?.let {
                                it(datas, buildResult())
                        }
                }
                val btCancel = rootView.findViewById<TextView>(R.id.cancel)
                btCancel.setOnClickListener {
                        onCancel?.let {
                                it(datas, buildResult())
                        }
                }
        }

        fun setData(datas: List<Any>) {
                val count = content.childCount
                if (count > 0) {
                        for (i in count - 1 until 0) {
                                content.removeViewAt(i)
                        }
                }
                datas.forEach {
                        val view = when (it) {
                                is SingleData -> SingleFilterView(context, it)
                                is MulData -> MultipleFilterView(context, it)
                                is InputData -> InputFilterView(context, it)
                                else -> null
                        }
                        view?.let {
                                content.addView(view)
                                children.add(view)
                        }
                }
                val btOK = rootView.findViewById<TextView>(R.id.ok)
                btOK.setOnClickListener {
                        onOk?.let {
                                it(datas, buildResult())
                        }
                }
                val btCancel = rootView.findViewById<TextView>(R.id.cancel)
                btCancel.setOnClickListener {
                        onCancel?.let {
                                it(datas, buildResult())
                        }
                }
        }

        private fun buildResult(): List<Any?> {
                val results = mutableListOf<Any?>()
                children.forEach {
                        when(it) {
                                is SingleFilterView -> results.add(it.selectedPosition)
                                is MultipleFilterView -> results.add(it.selectedPositions)
                                is InputFilterView -> results.add(it.result)
                        }
                }
                return results
        }
}