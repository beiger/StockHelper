package com.bing.stockhelper.collection.edit

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityArticleEditBinding
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
import kotlinx.android.synthetic.main.dialog_select_tags.view.*
import java.io.File
import java.lang.StringBuilder

class ArticleEditActivity : AppCompatActivity(), View.OnClickListener {

        private val REQUEST_CODE_CHOOSE_IMG = 0x00

        private lateinit var binding: ActivityArticleEditBinding
        private lateinit var viewModel: ArticleEditViewModel

        private var articleId: Int = -1
        private var imgUrl: String? = null
        private var firstTags = mutableListOf<Int>()
        private var secondTags = mutableListOf<Int>()

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_article_edit)
                viewModel = ViewModelProviders.of(this).get(ArticleEditViewModel::class.java)

                intent?.let{
                        articleId = it.getIntExtra(Constant.TAG_COLLECT_ARTICLE_ID, -1)
                }
                initViews()
        }

        private fun initViews() {
                ui {
                        waitIO {
                                viewModel.loadTags()
                                viewModel.load(articleId)
                        }
                        viewModel.collectArticle?.let {
                                binding.etTitle.setText(it.title)
                                binding.tvFirstTags.text = it.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                binding.tvSecondTags.text = it.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                imgUrl = it.imageUrl
                                firstTags = it.firstTags
                                secondTags = it.secondTags
                                Glide.with(this@ArticleEditActivity).load(it.imageUrl).into(binding.ivBg)
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

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK) {
                        when (requestCode) {
                                REQUEST_CODE_CHOOSE_IMG -> {
                                        val selected = PictureSelector.obtainMultipleResult(data)[0].path
                                        imgUrl = selected
                                        Glide.with(this).load(selected).into(binding.ivBg)
                                }
                        }
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
                textView.text = builder.dropLast(1).toString()
        }

        private fun addTags() {
                var dialog: BottomDialog? = null
                dialog = BottomDialog.create(
                        supportFragmentManager,
                        R.layout.dialog_add_tags,
                        handleView = {
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

        override fun onBackPressed() {
                if (!updateCollectArticle()) {
                        return
                }
                if (articleId == -1) {
                        viewModel.insert(viewModel.collectArticle!!)
                } else {
                        viewModel.update(viewModel.collectArticle!!)
                }
                super.onBackPressed()
        }

        private fun updateCollectArticle(): Boolean {
                if (imgUrl == null) {
                        ToastUtils.showShort(R.string.image_not_selected)
                        return false
                }
                val title = getStrFromEt(binding.etTitle) ?: return false
                if (viewModel.collectArticle == null) {
                        copyImage()
                        viewModel.collectArticle = CollectArticle(0, title, COLLECTION_TYPE_IMAGE, null, imgUrl, null, firstTags, secondTags)
                } else {
                        viewModel.collectArticle?.let {
                                if (it.imageUrl != imgUrl) {
                                        copyImage()
                                }
                                it.title = title
                                it.imageUrl = imgUrl
                                it.firstTags = firstTags
                                it.secondTags = secondTags
                        }
                }
                return true
        }

        private fun copyImage() {
                imgUrl?.let {
                        val dir = File(Constant.COLLECT_File_DIR)
                        dir.mkdirs()
                        var newImgUrl = Constant.COLLECT_File_DIR + it.substring(it.lastIndexOf("/"))
                        var newImg = File(newImgUrl)
                        if (newImg.exists()) {
                                newImgUrl = Constant.COLLECT_File_DIR + System.currentTimeMillis() + it.substring(it.lastIndexOf("."))
                                newImg = File(newImgUrl)
                        }
                        File(it).copyTo(newImg)
                        imgUrl = newImgUrl
                }
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

