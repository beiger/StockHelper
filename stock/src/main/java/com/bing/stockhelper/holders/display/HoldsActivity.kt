package com.bing.stockhelper.holders.display

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleStatePagerAdapter2
import com.bing.stockhelper.databinding.ActivityHoldsBinding
import com.bing.stockhelper.model.entity.OrderDetail
import com.fanhantech.baselib.utils.UiUtil

class HoldsActivity : AppCompatActivity() {
        private lateinit var binding: ActivityHoldsBinding
        private lateinit var viewModel: HoldsViewModel

        private var orderId = -1
        private var currentPosition = 0

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontWhite(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_holds)
                viewModel = ViewModelProviders.of(this).get(HoldsViewModel::class.java)

                intent?.let {
                        orderId = it.getIntExtra(Constant.TAG_ORDER_DETAIL_ID, -1)
                }
                initViews()
        }

        private fun initViews() {
                viewModel.orderDetailInfos.observe(this, Observer {
                        currentPosition = it.indexOfFirst { order -> order.id == orderId }
                        initViewPager(it)
                })
        }

        private fun initViewPager(orders: List<OrderDetail.DetailInfo>) {
                val pagerAdapter = SimpleStatePagerAdapter2(
                        supportFragmentManager,
                        { position -> HoldDetailFragment.instance(position) },
                        orders.size
                )
                with(binding.viewpager) {
                        adapter = pagerAdapter
                        addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
                                override fun onPageScrollStateChanged(state: Int) { }

                                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

                                override fun onPageSelected(position: Int) {
                                        currentPosition = position
                                }
                        })
                        setCurrentItem(currentPosition, false)
                }
        }
}
