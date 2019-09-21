package com.bing.stockhelper.stock

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityStockEditBinding
import com.bing.stockhelper.model.entity.StockDetail
import com.bing.stockhelper.utils.Constant
import com.bumptech.glide.Glide
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.utils.UiUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import java.text.SimpleDateFormat
import java.util.*

class StockEditActivity : AppCompatActivity() {
        private val REQUEST_CODE_CHOOSE_IMG = 0x00

        private lateinit var binding: ActivityStockEditBinding
        private lateinit var viewModel: StockEditViewModel

        private var stockId: Int = -1
        private var imgUrl: String? = null

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
                        val stockDetail = waitIO {
                                viewModel.load(stockId)
                        }
                        stockDetail?.let {
                                binding.etName.setText(it.name)
                                binding.etCode.setText(it.code)
                                binding.etTags.setText(it.tags)
                                binding.etComment.setText(it.comment)
                                imgUrl = it.imgUrl
                                Glide.with(this).load(it.imgUrl).into(binding.ivBg)
                        }
                }


                binding.back.setOnClickListener {
                        onBackPressed()
                }
                binding.cancel.setOnClickListener {
                        finish()
                }
                binding.ivBg.setOnClickListener {
                        PictureSelector.create(this)
                                .openGallery(PictureMimeType.ofImage())
                                .selectionMode(PictureConfig.SINGLE)
                                .isCamera(false)
                                .forResult(REQUEST_CODE_CHOOSE_IMG)
                }
        }
}
