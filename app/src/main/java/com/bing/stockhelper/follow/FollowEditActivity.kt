package com.bing.stockhelper.follow

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivityFollowEditBinding
import com.bing.stockhelper.model.entity.ItemFollow
import com.blankj.utilcode.util.ToastUtils
import com.fanhantech.baselib.kotlinExpands.afterTextChanged
import com.fanhantech.baselib.utils.UiUtil
import java.lang.Exception

class FollowEditActivity : AppCompatActivity() {

        private val REQUEST_CODE_CHOOSE_IMG = 0x00

        private lateinit var binding: ActivityFollowEditBinding
        private lateinit var viewModel: FollowEditViewModel
        @Volatile
        private var isNew = false
        private var itemFollow: ItemFollow? = null

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_follow_edit)
                viewModel = ViewModelProviders.of(this).get(FollowEditViewModel::class.java)

                if (intent == null) {
                        isNew = true
                } else {
                        itemFollow = intent.getParcelableExtra(Constant.TAG_ITEM_FOLLOW)
                        isNew = itemFollow == null
                }
                initViews()
        }

        private fun initViews() {
                itemFollow?.let {
                        binding.etName.setText(it.name)
                        binding.etCode.setText(it.code)
                        binding.etExpect.setText(it.expectPrice.toString())
                        binding.etCurrent.setText(it.currentPrice.toString())
                        binding.etFocusDegree.setText(it.focusDegree.toString())
                        binding.etType.setText(it.typeId.toString())
                        binding.etComment.setText(it.comment)
                }

                if (itemFollow == null) {
                        itemFollow = ItemFollow.instance()
                }

                binding.back.setOnClickListener {
                        onBackPressed()
                }
                binding.cancel.setOnClickListener {
                        finish()
                }
                binding.etName.afterTextChanged {
                        itemFollow!!.name = it.toString()
                }
                binding.etCode.afterTextChanged {
                        itemFollow!!.code = it.toString()
                }
                binding.etFocusDegree.afterTextChanged {
                        itemFollow!!.focusDegree = try {
                                it.toString().toInt()
                        } catch (e: Exception) {
                                ToastUtils.showShort(R.string.format_error)
                                0
                        }
                }
                binding.etType.afterTextChanged {
                        itemFollow!!.typeId =
                                try {
                                        it.toString().toInt()
                                } catch (e: Exception) {
                                        ToastUtils.showShort(R.string.format_error)
                                        -1
                                }
                }
                binding.etExpect.afterTextChanged {
                        itemFollow!!.expectPrice = try {
                                it.toString().toFloat()
                        } catch (e: Exception) {
                                ToastUtils.showShort(R.string.format_error)
                                0f
                        }
                }
                binding.etCurrent.afterTextChanged {
                        itemFollow!!.currentPrice = try {
                                it.toString().toFloat()
                        } catch (e: Exception) {
                                ToastUtils.showShort(R.string.format_error)
                                0f
                        }
                }
                binding.etComment.afterTextChanged {
                        itemFollow!!.comment = it.toString()
                }
        }

        override fun onBackPressed() {
                if (isNew) {
                        viewModel.insert(itemFollow!!)
                } else {
                        viewModel.update(itemFollow!!)
                }
                super.onBackPressed()
        }
}
