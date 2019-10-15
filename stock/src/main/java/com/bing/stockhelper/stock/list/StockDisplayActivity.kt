package com.bing.stockhelper.stock.list

import android.graphics.Color
import android.os.Bundle
import android.view.View
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
import com.blankj.utilcode.util.SizeUtils
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import org.jetbrains.anko.startActivity

class StockDisplayActivity : AppCompatActivity(), View.OnClickListener {

        private lateinit var binding: ActivityStockDisplayBinding
        private lateinit var viewModel: StockDisplayViewModel
        private var stockId: Int = 0
        private lateinit var mainColor: IntArray

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_display)
                UiUtil.setBarColorAndFontWhiteByChangeView(this, Color.TRANSPARENT, binding.toolbar)
                initSlideBack()
                viewModel = ViewModelProviders.of(this).get(StockDisplayViewModel::class.java)

                intent?.let {
                        stockId = it.getIntExtra(Constant.TAG_STOCK_ID, -1)
                        mainColor = it.getIntArrayExtra(Constant.TAG_MAIN_COLOR)!!
                }
                binding.clHead.setBackgroundColor(Color.argb(200, mainColor[0], mainColor[1], mainColor[2]))
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

}
