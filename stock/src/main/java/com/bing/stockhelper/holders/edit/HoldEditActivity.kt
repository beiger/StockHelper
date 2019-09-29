package com.bing.stockhelper.holders.edit

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityHoldEditBinding
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.stock.StockListActivity
import com.blankj.utilcode.util.ToastUtils
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import org.jetbrains.anko.startActivityForResult
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class HoldEditActivity : AppCompatActivity(), View.OnClickListener {

        private val REQUEST_CODE_CHOOSE_STOCK = 0x00

        private lateinit var binding: ActivityHoldEditBinding
        private lateinit var viewModel: HoldEditViewModel

        private var orderDetailId: Int = -1
        private var time = 0L

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_hold_edit)
                viewModel = ViewModelProviders.of(this).get(HoldEditViewModel::class.java)

                intent?.let {
                        orderDetailId = intent.getIntExtra(Constant.TAG_ORDER_DETAIL_ID, -1)
                }
                initViews()
        }

        private fun initViews() {
                ui {
                        waitIO {
                                viewModel.loadOrder(orderDetailId)
                                viewModel.orderDetail?.let {
                                        viewModel.loadStock(it.stockId)
                                }
                        }
                        viewModel.orderDetail?.let {
                                binding.tvStock.text = viewModel.stockDetail?.name
                                binding.etHoldNum.setText(it.buyNum.toString())
                                time = it.buyTime
                                binding.etBuyTime.text = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(Date(it.buyTime))
                                binding.etCost.setText(it.buyPrice.toString())
                                binding.etExpect.setText(it.expectPrice.toString())
                                binding.etCurrent.setText(it.currentPrice.toString())
                                binding.etSellPrice.setText(it.sellPrice?.toString())
                                binding.etComment.setText(it.comment)
                        }
                }

                addClickableViews(
                        binding.back,
                        binding.cancel,
                        binding.etBuyTime,
                        binding.tvStock
                )
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> onBackPressed()
                        R.id.cancel -> finish()
                        R.id.etBuyTime -> datePick()
                        R.id.tvStock -> startActivityForResult<StockListActivity>(REQUEST_CODE_CHOOSE_STOCK)
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
                                REQUEST_CODE_CHOOSE_STOCK -> {
                                        val stockId = data?.getIntExtra(Constant.TAG_STOCK_ID, -1) ?: return
                                        if (stockId != -1) {
                                                ui {
                                                        waitIO {
                                                                viewModel.loadStock(stockId)
                                                        }
                                                        binding.tvStock.text = viewModel.stockDetail?.name
                                                }
                                        }
                                }
                        }
                }
        }

        override fun onBackPressed() {
                if (!updateOrderDetail()) {
                        return
                }
                if (orderDetailId == -1) {
                        viewModel.insert(viewModel.orderDetail!!)
                } else {
                        viewModel.update(viewModel.orderDetail!!)
                }
                super.onBackPressed()
        }

        private fun updateOrderDetail(): Boolean {
                val stockId = viewModel.stockDetail?.id
                if (stockId == null) {
                        ToastUtils.showShort(R.string.stock_empty)
                        return false
                }
                val buyPrice = getFloatFromEt(binding.etCost) ?: return false
                val holdNum = getIntFromEt(binding.etHoldNum) ?: return false
                val expectPrice = getFloatFromEt(binding.etExpect) ?: return false
                val currentPrice = getFloatFromEt(binding.etCurrent) ?: return false
                val sellPrice = getFloatFromEt(binding.etSellPrice, false)
                val comment = getStrFromEt(binding.etComment, false)
                if (time == 0L) {
                        ToastUtils.showShort(R.string.time_empty)
                        return false
                }
                if (viewModel.orderDetail == null) {
                        viewModel.orderDetail = OrderDetail.instance().apply { orderNum = System.currentTimeMillis() }
                }
                viewModel.orderDetail?.let {
                        it.stockId = stockId
                        it.buyTime = time
                        it.buyPrice = buyPrice
                        it.expectPrice = expectPrice
                        it.currentPrice = currentPrice
                        it.sellPrice = sellPrice
                        if (sellPrice == null) {
                                it.isHold = false
                        }
                        it.buyNum = holdNum
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
