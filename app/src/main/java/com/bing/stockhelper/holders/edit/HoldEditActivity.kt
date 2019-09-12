package com.bing.stockhelper.holders.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityHoldEditBinding
import com.bing.stockhelper.model.entity.OrderDetail
import com.blankj.utilcode.util.ToastUtils
import com.bumptech.glide.Glide
import com.fanhantech.baselib.utils.UiUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class HoldEditActivity : AppCompatActivity() {

        private val REQUEST_CODE_CHOOSE_IMG = 0x00

        private lateinit var binding: ActivityHoldEditBinding
        private lateinit var viewModel: HoldEditViewModel
        @Volatile
        private var isNew = false
        private var orderDetail: OrderDetail? = null
        private var time = 0L
        private var imgUrl: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_hold_edit)
                viewModel = ViewModelProviders.of(this).get(HoldEditViewModel::class.java)

                if (intent == null) {
                        isNew = true
                } else {
                        orderDetail = intent.getParcelableExtra(Constant.TAG_ORDER_DETAIL)
                        isNew = orderDetail == null
                }
                initViews()
        }

        private fun initViews() {
                orderDetail?.let {
                        binding.etName.setText(it.name)
                        binding.etCode.setText(it.code)
                        binding.etHoldNum.setText(it.buyNum.toString())
                        time = it.buyTime
                        binding.etBuyTime.text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(it.buyTime))
                        binding.etCost.setText(it.buyPrice.toString())
                        binding.etExpect.setText(it.expectPrice.toString())
                        binding.etCurrent.setText(it.currentPrice.toString())
                        binding.etSellPrice.setText(it.sellPrice?.toString())
                        binding.etTodayOp.setText(it.todayOp)
                        binding.etTomorrowOp.setText(it.tomorrowOp)
                        binding.etComment.setText(it.comment)
                        imgUrl = it.imgUrl
                        Glide.with(this).load(it.imgUrl).into(binding.ivBg)
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

                binding.etBuyTime.setOnClickListener {
                        datePick()
                }
        }

        private fun datePick() {
                val ca = Calendar.getInstance()
                val year = ca.get(Calendar.YEAR)
                val month = ca.get(Calendar.MONTH)
                val day = ca.get(Calendar.DAY_OF_MONTH)
                DatePickerDialog(this, DatePickerDialog.OnDateSetListener { _, y, m, d ->
                        ca.set(y, m, d)
                        time = ca.timeInMillis
                        binding.etBuyTime.text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(ca.timeInMillis))
                }, year, month, day).show()
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK) {
                        when (requestCode) {
                                REQUEST_CODE_CHOOSE_IMG -> {
                                        val path: String? = PictureSelector.obtainMultipleResult(data)[0].path
                                        imgUrl = path
                                        Glide.with(this).load(path).into(binding.ivBg)
                                }
                        }
                }
        }

        override fun onBackPressed() {
                if (!updateOrderDetail()) {
                        return
                }
                if (isNew) {
                        viewModel.insert(orderDetail!!)
                } else {
                        viewModel.update(orderDetail!!)
                }
                setResult(Activity.RESULT_OK, Intent().apply {
                        putExtra(Constant.TAG_ORDER_DETAIL, orderDetail)
                })
                super.onBackPressed()
        }

        private fun updateOrderDetail(): Boolean {
                val name = getStrFromEt(binding.etName) ?: return false
                val code = getStrFromEt(binding.etCode) ?: return false
                val buyPrice = getFloatFromEt(binding.etCost) ?: return false
                val holdNum = getIntFromEt(binding.etHoldNum) ?: return false
                val expectPrice = getFloatFromEt(binding.etExpect) ?: return false
                val currentPrice = getFloatFromEt(binding.etCurrent) ?: return false
                val sellPrice = getFloatFromEt(binding.etSellPrice, false)
                val todayOp = getStrFromEt(binding.etTodayOp, false)
                val tomorrowOp = getStrFromEt(binding.etTomorrowOp, false)
                val comment = getStrFromEt(binding.etComment, false)
                if (time == 0L) {
                        ToastUtils.showShort(R.string.time_empty)
                        return false
                }
                if (orderDetail == null) {
                        orderDetail = OrderDetail.instance().apply { orderNum = System.currentTimeMillis() }
                }
                orderDetail?.let {
                        println("----$imgUrl")
                        it.code = code
                        it.name = name
                        it.imgUrl = imgUrl
                        it.buyTime = time
                        it.buyPrice = buyPrice
                        it.expectPrice = expectPrice
                        it.currentPrice = currentPrice
                        it.sellPrice = sellPrice
                        if (sellPrice == null) {
                                it.isHold = false
                        }
                        it.buyNum = holdNum
                        it.todayOp = todayOp
                        it.tomorrowOp = tomorrowOp
                        it.comment = comment
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

        private fun getFloatFromEt(et: EditText, required: Boolean = true): Float? {
                if (et.text.toString().isEmpty()) {
                        if (required) {
                                ToastUtils.showShort(R.string.info_not_complete)
                        }
                        return null
                }
                return try {
                        et.text.toString().toFloat()
                } catch (e: Exception) {
                        ToastUtils.showShort(R.string.format_error)
                        null
                }
        }

        private fun getIntFromEt(et: EditText): Int? {
                return try {
                        et.text.toString().toInt()
                } catch (e: Exception) {
                        ToastUtils.showShort(R.string.format_error)
                        null
                }
        }
}
