package com.bing.stockhelper.stock

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityStockEditBinding
import com.bing.stockhelper.model.entity.*
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.widget.filtersview.MulData
import com.bing.stockhelper.widget.filtersview.MultipleFilterView
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import com.fanhantech.bottomdialog.BottomDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.dialog_add_tags.view.*
import kotlinx.android.synthetic.main.dialog_add_tags.view.tvOk
import kotlinx.android.synthetic.main.dialog_select_tags.view.*
import kotlinx.android.synthetic.main.dialog_select_tags.view.tvCancel
import java.lang.StringBuilder

class StockEditActivity : AppCompatActivity(), View.OnClickListener {

        private val REQUEST_CODE_CHOOSE_IMG = 0x00

        private lateinit var binding: ActivityStockEditBinding
        private lateinit var viewModel: StockEditViewModel

        private var stockId: Int = -1
        private var imgUrl: String? = null
        private var firstTags = mutableListOf<Int>()
        private var secondTags = mutableListOf<Int>()

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_edit)
                viewModel = ViewModelProviders.of(this).get(StockEditViewModel::class.java)

                intent?.let{
                        stockId = it.getIntExtra(Constant.TAG_STOCK_ID, -1)
                }
                initViews()
        }

        private fun initViews() {
                ui {
                        waitIO {
                                viewModel.loadTags()
                                viewModel.load(stockId)
                        }
                        viewModel.stockDetail?.let {
                                binding.etName.setText(it.name)
                                binding.etCode.setText(it.code)
                                binding.tvFirstTags.text = it.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                binding.tvSecondTags.text = it.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                binding.etDescription.setText(it.description)
                                imgUrl = it.imgUrl
                                Glide.with(this@StockEditActivity).load(it.imgUrl).into(binding.ivBg)
                        }
                }

                viewModel.stockTagLive.observe(this, Observer { tags ->
                        viewModel.stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                        viewModel.stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
                })
                addClickableViews(
                        binding.tvFirstTags,
                        binding.tvSecondTags,
                        binding.back,
                        binding.cancel,
                        binding.ivBg
                )
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.tvFirstTags -> clickTags(TAG_LEVEL_FIRST)
                        R.id.tvSecondTags -> clickTags(TAG_LEVEL_SECOND)
                        R.id.ivBg -> PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofImage())
                                .selectionMode(PictureConfig.SINGLE)
                                .isCamera(false)
                                .forResult(REQUEST_CODE_CHOOSE_IMG)
                        R.id.back -> onBackPressed()
                        R.id.cancel -> finish()
                }
        }

        private fun clickTags(level: Int) {
                val title: String
                val tags: List<StockTag>
                when (level) {
                        TAG_LEVEL_FIRST -> {
                                title = getString(R.string.tag_first_level)
                                tags = viewModel.stockTagsFirst
                        }
                        TAG_LEVEL_SECOND -> {
                                title = getString(R.string.tag_second_level)
                                tags = viewModel.stockTagsSecond
                        }
                        else -> {
                                title = ""
                                tags = mutableListOf()
                        }
                }
                val names = getNamesFromTags(tags)
                val data = MulData(title, names)
                val view = MultipleFilterView(this, data)
                var dialog: BottomDialog? = null
                dialog = BottomDialog.create(
                        supportFragmentManager,
                        R.layout.dialog_select_tags,
                        handleView = {
                                it.container.addView(view)
                                it.tvOk.setOnClickListener {
                                        selectTags(level, tags, view.selectedPositions)
                                        dialog?.dismiss()
                                }
                                it.tvCancel.setOnClickListener {
                                        dialog?.dismiss()
                                }
                                it.tvAddTag.setOnClickListener {
                                        dialog?.dismiss()
                                        addTags()
                                }
                        }
                 )
                dialog.show()
        }

        private fun getNamesFromTags(tags: List<StockTag>): MutableList<String> {
                val names = mutableListOf<String>()
                tags.forEach {
                        names.add(it.name)
                }
                return names
        }

        private fun selectTags(level: Int, tags: List<StockTag>, results: List<Int>) {
                var choosedTags: MutableList<Int>? = null
                var textView: TextView? = null
                when (level) {
                        TAG_LEVEL_FIRST -> {
                                choosedTags = firstTags
                                textView = binding.tvFirstTags
                        }
                        TAG_LEVEL_SECOND -> {
                                choosedTags = secondTags
                                textView = binding.tvSecondTags
                        }
                        else -> {}
                }
                choosedTags ?: return
                textView ?: return
                choosedTags.clear()
                if (results.isEmpty()) {
                        textView.text = ""
                        return
                }
                val builder = StringBuilder()
                results.forEach {
                        choosedTags.add(tags[it].id)
                        builder.append(tags[it].name + TAG_SEPERATER)
                }
                builder.dropLast(1)
                textView.text = builder.toString()
        }

        private fun addTags() {
                var dialog: BottomDialog? = null
                dialog = BottomDialog.create(
                        supportFragmentManager,
                        R.layout.dialog_add_tags,
                        handleView = { it ->
                                val radioGroup = it.findViewById<RadioGroup>(R.id.radio_group)
                                val tvOk = it.findViewById<TextView>(R.id.tvOk)
                                tvOk.setOnClickListener {
                                        val level = when (radioGroup.checkedRadioButtonId) {
                                                R.id.rbFirst -> TAG_LEVEL_FIRST
                                                R.id.rbSecond -> TAG_LEVEL_SECOND
                                                else -> null
                                        }
                                        val etTags = it.findViewById<EditText>(R.id.etTags)
                                        val names = etTags.text.toString()
                                        if (level == null) {
                                                ToastUtils.showShort(R.string.level_not_selected)
                                                return@setOnClickListener
                                        }
                                        addTags(level, names)
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
                                viewModel.insertTag(StockTag(-1, it, level))
                        }
                }
        }

        override fun onBackPressed() {
                if (!updateStockDetail()) {
                        return
                }
                if (stockId == -1) {
                        viewModel.insert(viewModel.stockDetail!!)
                } else {
                        viewModel.update(viewModel.stockDetail!!)
                }
                super.onBackPressed()
        }

        private fun updateStockDetail(): Boolean {
                val name = getStrFromEt(binding.etName) ?: return false
                val code = getStrFromEt(binding.etCode) ?: return false
                val description = getStrFromEt(binding.etDescription, false)
                if (viewModel.stockDetail == null) {
                        viewModel.stockDetail = StockDetail(-1, code, name, imgUrl, firstTags, secondTags, description)
                } else {
                        viewModel.stockDetail?.let {
                                it.code = code
                                it.name = name
                                it.imgUrl = imgUrl
                                it.firstTags = firstTags
                                it.secondTags = secondTags
                                it.description = description
                        }
                }
                return true
        }

        private fun getStrFromEt(et: EditText, required: Boolean = true): String? {
                val value = et.text.toString()
                return if (value.isEmpty()) {
                        if (required) {
                                ToastUtils.showShort(R.string.info_not_complete)
                        }
                        null
                } else {
                        value
                }
        }
}
