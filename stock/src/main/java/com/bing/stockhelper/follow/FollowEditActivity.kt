package com.bing.stockhelper.follow

import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
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
                        binding.etTags.setText(it.tags.toString())
                        binding.etComment.setText(it.comment)
                }

                binding.back.setOnClickListener {
                        onBackPressed()
                }
                binding.cancel.setOnClickListener {
                        finish()
                }
        }

        override fun onBackPressed() {
                if (!updateItemFollow()) {
                        return
                }
                if (isNew) {
                        viewModel.insert(itemFollow!!)
                } else {
                        viewModel.update(itemFollow!!)
                }
                super.onBackPressed()
        }

        private fun updateItemFollow(): Boolean {
                val name = getStrFromEt(binding.etName) ?: return false
                val code = getStrFromEt(binding.etCode) ?: return false
                val expectPrice = getFloatFromEt(binding.etExpect) ?: return false
                val currentPrice = getFloatFromEt(binding.etCurrent) ?: return false
                val comment = getStrFromEt(binding.etComment, false)
                val focusDegree = getIntFromEt(binding.etFocusDegree) ?: return false
                val tags = getStrFromEt(binding.etTags) ?: return false
                if (itemFollow == null) {
                        itemFollow = ItemFollow.instance()
                }
                itemFollow?.let {
                        it.code = code
                        it.name = name
                        it.expectPrice = expectPrice
                        it.currentPrice = currentPrice
                        it.comment = comment
                        it.focusDegree = focusDegree
                        it.tags = tags
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
