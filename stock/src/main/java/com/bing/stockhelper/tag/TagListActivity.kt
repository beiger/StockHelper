package com.bing.stockhelper.tag

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import com.adorkable.iosdialog.AlertDialog
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.input.input
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.databinding.ActivityTagListBinding
import com.bing.stockhelper.databinding.ItemTagBinding
import com.bing.stockhelper.model.entity.StockTag
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.bing.stockhelper.model.entity.TAG_SEPERATER
import com.blankj.utilcode.util.ToastUtils
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import com.fanhantech.bottomdialog.BottomDialog
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.dialog_select_tags.view.*

class TagListActivity : AppCompatActivity(), View.OnClickListener {
        private lateinit var binding: ActivityTagListBinding
        private lateinit var viewModel: TagListViewModel
        private lateinit var mAdapter: SimpleAdapter<StockTag, ItemTagBinding>

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_tag_list)
                viewModel = ViewModelProviders.of(this).get(TagListViewModel::class.java)

                initView()
        }

        private fun initView() {
                with(binding.refreshLayout) {
                        setEnableRefresh(false)
                        setEnableLoadMore(false)
                        setEnableOverScrollDrag(true)//是否启用越界拖动
                }
                with(binding.recyclerView) {
                        layoutManager = FlexboxLayoutManager(this@TagListActivity).apply {
                                flexDirection = FlexDirection.ROW
                                flexWrap = FlexWrap.WRAP
                                justifyContent = JustifyContent.FLEX_START
                        }
                        itemAnimator = DefaultItemAnimator()
                }
                initAdapter()
                viewModel.stockTags.observe(this, Observer {
                        mAdapter.update(it)
                })

                addClickableViews(
                        binding.back,
                        binding.fabAdd
                )
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        onClick = {
                                MaterialDialog(this).show {
                                        input(prefill = it.name) { _, text ->
                                                val newName = text.toString()
                                                if (newName.isNotEmpty()) {
                                                        viewModel.update(it.copy().apply { name = newName })
                                                }
                                        }
                                        positiveButton {}
                                }
                        },
                        isSame = { old, newI -> old.isSameWith(newI) },
                        itemLayout = R.layout.item_tag,
                        bindData = { item, binding ->
                                binding.item = item
                                binding.tvTag.textSize = when (item.level) {
                                        TAG_LEVEL_FIRST -> 15f
                                        TAG_LEVEL_SECOND -> 12f
                                        else -> 12f
                                }
                                binding.root.setOnLongClickListener {
                                        AlertDialog(this)
                                                .init()
                                                .setMsg(getString(R.string.confirm_delete))
                                                .setPositiveButton("") {
                                                        viewModel.delete(item)
                                                }.setNegativeButton("") {

                                                }.show()
                                        true
                                }
                        }
                )
                binding.recyclerView.adapter = mAdapter
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> finish()
                        R.id.fabAdd -> addTags()
                }
        }

        private fun addTags() {
                var dialog: BottomDialog? = null
                dialog = BottomDialog.create(
                        supportFragmentManager,
                        R.layout.dialog_add_tags,
                        handleView = {
                                it.findViewById<RadioButton>(R.id.rbFirst).setButtonDrawable(R.drawable.radio_button_style)
                                it.findViewById<RadioButton>(R.id.rbSecond).setButtonDrawable(R.drawable.radio_button_style)
                                val radioGroup = it.findViewById<RadioGroup>(R.id.radio_group)
                                val tvOk = it.findViewById<TextView>(R.id.tvOk)
                                val etTags = it.findViewById<EditText>(R.id.etTags)
                                tvOk.setOnClickListener {
                                        val level = when (radioGroup.checkedRadioButtonId) {
                                                R.id.rbFirst -> TAG_LEVEL_FIRST
                                                R.id.rbSecond -> TAG_LEVEL_SECOND
                                                else -> null
                                        }
                                        val names = etTags.text.toString()
                                        if (names.isEmpty()) {
                                                ToastUtils.showShort(R.string.no_tags)
                                                return@setOnClickListener
                                        }
                                        if (level == null) {
                                                ToastUtils.showShort(R.string.level_not_selected)
                                                return@setOnClickListener
                                        }
                                        addTags(level, names)
                                        dialog?.dismiss()
                                }
                                it.tvCancel.setOnClickListener {
                                        dialog?.dismiss()
                                }
                        }
                )
                dialog.show()
        }

        private fun addTags(level: Int, names: String) {
                io {
                        names.split(TAG_SEPERATER).forEach {
                                viewModel.insertTag(StockTag(0, it, level))
                        }
                }
        }
}