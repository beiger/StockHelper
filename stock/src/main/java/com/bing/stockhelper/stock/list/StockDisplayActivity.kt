package com.bing.stockhelper.stock.list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityStockDisplayBinding

class StockDisplayActivity : AppCompatActivity() {
        private lateinit var binding: ActivityStockDisplayBinding

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_display)
        }
}
