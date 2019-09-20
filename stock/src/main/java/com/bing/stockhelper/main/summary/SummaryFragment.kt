package com.bing.stockhelper.main.summary

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.adorkable.iosdialog.AlertDialog
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.FragmentSummaryBinding
import com.bing.stockhelper.model.entity.Summary
import com.bing.stockhelper.summary.SummaryEditActivity
import com.bing.stockhelper.summary.SummaryViewModel
import com.bing.stockhelper.utils.Constant
import com.stone.card.library.CardAdapter
import com.stone.card.library.CardSlidePanel
import java.text.SimpleDateFormat
import java.util.*

class SummaryFragment : Fragment() {
        private lateinit var binding: FragmentSummaryBinding
        private lateinit var viewModel: SummaryViewModel

        private var summaries = mutableListOf<Summary>()
        private var summariesTemp = mutableListOf<Summary>()

        private var bgIndex = 0

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
                binding = DataBindingUtil.inflate(inflater, R.layout.fragment_summary, container, false)
                viewModel = ViewModelProviders.of(this).get(SummaryViewModel::class.java)
                initView()
                return binding.root
        }

        private fun initView() {
                viewModel.summaries.observe(this, Observer {
                        println("-------summary change")
                        summaries.clear()
                        summaries.addAll(it)

                        summariesTemp.clear()
                        summariesTemp.addAll(it)

                        if (binding.slidePanel.adapter == null) {
                               val adapter = object : CardAdapter() {
                                        override fun getCount(): Int {
                                                return summaries.size
                                        }

                                        override fun bindView(view: View, index: Int) {
                                                val content = view.findViewById<LinearLayout>(R.id.card_item_content)
                                                if (bgIndex++ % 2 == 0) {
                                                        content.setBackgroundColor(resources.getColor(R.color.white))
                                                } else {
                                                        content.setBackgroundResource(R.drawable.bg_edit5)
                                                }
                                                val tag: Any? = view.tag
                                                val viewHolder: SummaryViewHolder
                                                if (null != tag) {
                                                        viewHolder = tag as SummaryViewHolder
                                                } else {
                                                        viewHolder = SummaryViewHolder(view)
                                                        view.tag = viewHolder
                                                }

                                                viewHolder.bindData(summaries[index])
                                        }

                                        override fun getItem(index: Int): Any {
                                                return summaries[index]
                                        }

                                        override fun getLayoutId(): Int {
                                                return R.layout.item_summary
                                        }
                                }
                                binding.slidePanel.adapter = adapter
                                binding.slidePanel.setCardSwitchListener(object : CardSlidePanel.CardSwitchListener {
                                        override fun onShow(index: Int) {

                                        }

                                        override fun onCardVanish(index: Int, type: Int) {

                                        }
                                })
                        } else {
                                binding.slidePanel.adapter.notifyDataSetChanged()
                        }
                })
        }

        inner class SummaryViewHolder(val view: View) {
                private var content: TextView = view.findViewById(R.id.tvContent)
                private var date: TextView = view.findViewById(R.id.tvDate)

                fun bindData(summary: Summary) {
                        content.text = summary.content
                        date.text = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(summary.lastEditTime))
                        view.setOnClickListener {
                                val intent = Intent(view.context, SummaryEditActivity::class.java)
                                intent.putExtra(Constant.TAG_SUMMARY, summary)
                                view.context.startActivity(intent)
                        }
                        view.setOnLongClickListener {
                                AlertDialog(view.context)
                                        .init()
                                        .setMsg(getString(R.string.confirm_delete))
                                        .setPositiveButton("") {
                                                viewModel.delete(summary)
                                        }.setNegativeButton("") {

                                        }.show()
                                true
                        }
                }
        }
}
