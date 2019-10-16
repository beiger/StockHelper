package com.bing.stockhelper.stock.list

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityStockDisplayBinding
import com.bing.stockhelper.kotlinexpands.initSlideBack
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.bing.stockhelper.stock.edit.StockEditActivity
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.utils.MMCQ
import com.bing.stockhelper.utils.gaussianBlur
import com.blankj.utilcode.util.SizeUtils
import com.bumptech.glide.Glide
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.BitmapUtil
import com.fanhantech.baselib.utils.UiUtil
import org.jetbrains.anko.startActivity
import java.io.File
import java.io.IOException
import java.util.ArrayList

class StockDisplayActivity : AppCompatActivity(), View.OnClickListener {

        private lateinit var binding: ActivityStockDisplayBinding
        private lateinit var viewModel: StockDisplayViewModel
        private var stockId: Int = 0
        private var mainColor: IntArray = intArrayOf(0, 0, 0)

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_display)
                UiUtil.setBarColorAndFontWhiteByChangeView(this, Color.TRANSPARENT, binding.toolbar)
                initSlideBack()
                viewModel = ViewModelProviders.of(this).get(StockDisplayViewModel::class.java)

                intent?.let {
                        stockId = it.getIntExtra(Constant.TAG_STOCK_ID, -1)
                }
                binding.scrollView.setOnScrollChangeListener(object : View.OnScrollChangeListener {
                        val headHeight = SizeUtils.dp2px(175f)
                        override fun onScrollChange(v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                                val alpha = if (scrollY > headHeight) {
                                        255
                                } else {
                                        scrollY * 255 / headHeight
                                }
                                binding.toolbar.setBackgroundColor(Color.argb(alpha, mainColor[0], mainColor[1], mainColor[2]))
                        }
                })
                ui {
                        waitIO {
                                viewModel.loadTags()
                        }
                        viewModel.load(stockId)
                        viewModel.stocks.observe(this@StockDisplayActivity, Observer {
                                if (it.isNotEmpty()) {
                                        val item = it[0]
                                        binding.item = item
                                        binding.flTags.text = item.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                        binding.slTags.text = item.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                        ui {
                                                setGaussianBlur(item.imgUrl)
                                                io { mainColor = getMainColor(item.imgUrl) }
                                        }
                                }
                        })
                        viewModel.stockTagLive.observe(this@StockDisplayActivity, Observer { tags ->
                                viewModel.stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                                viewModel.stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
                        })
                }
                addClickableViews(
                        binding.back,
                        binding.ivBg
                )
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> finish()

                        R.id.ivBg ->  startActivity<StockEditActivity>(Constant.TAG_STOCK_ID to stockId)
                }
        }

        @WorkerThread
        private fun getMainColor(imageFileName: String?): IntArray {
                imageFileName ?: return IntArray(3) { 0 }
                val bitmap = BitmapUtil.decodeSampledBitmapFromFile(imageFileName, 500, 500) ?: return intArrayOf(0, 0, 0)
                var result: List<IntArray> = ArrayList()
                try {
                        result = MMCQ.compute(bitmap, 3)
                } catch (e: IOException) {
                        e.printStackTrace()
                }
                bitmap.recycle()
                return result[0]
        }

        @MainThread
        private fun setGaussianBlur(imageFileName: String?) {
                imageFileName ?: return
                val fileGs = imageFileName + "_gs"
                println("------$imageFileName, $fileGs")
                ui {
                        waitIO {
                                if (!File(fileGs).exists()) {
                                        gaussianBlur(this@StockDisplayActivity, imageFileName, fileGs)
                                }
                        }
                        Glide.with(this@StockDisplayActivity).load(fileGs).into(binding.bgHead)
                }
        }

}
