package com.bing.stockhelper.summary

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivitySummaryEditBinding
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.Summary
import com.bing.stockhelper.utils.Constant
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.kotlinExpands.afterTextChanged
import com.fanhantech.baselib.utils.UiUtil

class SummaryEditActivity : AppCompatActivity(), View.OnClickListener {

        private lateinit var binding: ActivitySummaryEditBinding
        private var summary: Summary? = null
        private var content: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_summary_edit)
                intent?.let {
                        summary = it.getParcelableExtra(Constant.TAG_SUMMARY) as? Summary
                        binding.etSummary.setText(summary?.content)
                        content = summary?.content
                }

                addClickableViews(binding.back, binding.cancel)
                binding.etSummary.afterTextChanged {
                        content = it.toString()
                }
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> onBackPressed()

                        R.id.cancel -> finish()
                }
        }

        override fun onBackPressed() {
                io {
                        if (summary == null) {
                                summary = Summary(0, content ?: "", System.currentTimeMillis())
                                AppDatabase.getInstance(this@SummaryEditActivity).insertSummary(summary!!)
                        } else {
                                summary!!.content = content ?: ""
                                summary!!.lastEditTime = System.currentTimeMillis()
                                AppDatabase.getInstance(this@SummaryEditActivity).updateSummary(summary!!)
                        }
                }
                super.onBackPressed()
        }
}
