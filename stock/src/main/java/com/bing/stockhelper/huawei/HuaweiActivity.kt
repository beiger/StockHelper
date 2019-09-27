package com.bing.stockhelper.huawei

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityHuaweiBinding

class HuaweiActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHuaweiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_huawei)
    }
}
