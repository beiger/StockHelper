package com.bing.stockhelper.follow

import android.app.Activity
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
import com.bing.stockhelper.databinding.ActivityFollowEditBinding
import com.bing.stockhelper.model.entity.ItemFollow
import com.bing.stockhelper.stock.StockListActivity
import com.blankj.utilcode.util.ToastUtils
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import org.jetbrains.anko.startActivityForResult
import java.lang.Exception

class FollowEditActivity : AppCompatActivity(), View.OnClickListener {

        private val REQUEST_CODE_CHOOSE_STOCK = 0x00

        private lateinit var binding: ActivityFollowEditBinding
        private lateinit var viewModel: FollowEditViewModel

        private var followId: Int = -1

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_follow_edit)
                viewModel = ViewModelProviders.of(this).get(FollowEditViewModel::class.java)

                intent?.let {
                        followId = intent.getIntExtra(Constant.TAG_ITEM_FOLLOW_ID, -1)
                }
                initViews()
        }

        private fun initViews() {
                ui {
                        waitIO {
                                viewModel.loadFollow(followId)
                                viewModel.itemFollow?.let {
                                        viewModel.loadStock(it.stockId)
                                }
                        }
                        viewModel.itemFollow?.let {
                                binding.tvStock.text = viewModel.stockDetail?.name
                                binding.etFocusDegree.setText(it.focusDegree.toString())
                                binding.etComment.setText(it.comment)
                        }
                }

                addClickableViews(
                        binding.back,
                        binding.cancel,
                        binding.tvStock
                )
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> onBackPressed()
                        R.id.cancel -> finish()
                        R.id.tvStock -> startActivityForResult<StockListActivity>(REQUEST_CODE_CHOOSE_STOCK)
                }
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
                if (!updateItemFollow()) {
                        return
                }
                if (followId == -1) {
                        viewModel.insert(viewModel.itemFollow!!)
                } else {
                        viewModel.update(viewModel.itemFollow!!)
                }
                super.onBackPressed()
        }

        private fun updateItemFollow(): Boolean {
                val stockId = viewModel.stockDetail?.id
                if (stockId == null) {
                        ToastUtils.showShort(R.string.stock_empty)
                        return false
                }
                val comment = getStrFromEt(binding.etComment, false)
                val focusDegree = getIntFromEt(binding.etFocusDegree) ?: return false
                if (viewModel.itemFollow == null) {
                        viewModel.itemFollow = ItemFollow.instance()
                }
                viewModel.itemFollow?.let {
                        it.stockId = stockId
                        it.comment = comment
                        it.focusDegree = focusDegree
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

        private fun getIntFromEt(et: EditText): Int? {
                return try {
                        et.text.toString().toInt()
                } catch (e: Exception) {
                        ToastUtils.showShort(R.string.format_error)
                        null
                }
        }
}
