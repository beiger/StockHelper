package com.bing.stockhelper.holders.display

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.utils.Constant.TAG_POSITION
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.FragmentHoldDetailBinding
import com.bing.stockhelper.holders.edit.HoldEditActivity
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.bing.stockhelper.utils.Constant
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import org.jetbrains.anko.support.v4.startActivityForResult

class HoldDetailFragment : Fragment() {

        private lateinit var binding: FragmentHoldDetailBinding
        private lateinit var viewModel: HoldsViewModel
        private var position: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                arguments?.let {
                        position = it.getInt(TAG_POSITION)
                }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
                binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hold_detail, container, false)
                viewModel = ViewModelProviders.of(activity!!).get(HoldsViewModel::class.java)
                val order = viewModel.orderDetailInfos.value!![position]
                binding.order = order
                ui {
                        waitIO { viewModel.loadTags() }
                        binding.flTags.text = order.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                        binding.slTags.text = order.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                }
                binding.cardView.setOnClickListener {
                        val item = viewModel.orderDetailInfos.value!![position]
                        startActivityForResult<HoldEditActivity>(REQUEST_CODE_EDIT, Constant.TAG_ORDER_DETAIL_ID to item.id)
                }
                return binding.root
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK) {
                        when (requestCode) {
                                REQUEST_CODE_EDIT -> {

                                }
                        }
                }
        }

        companion object {
                private const val REQUEST_CODE_EDIT = 0

                fun instance(position: Int): HoldDetailFragment {
                        return HoldDetailFragment().apply {
                                arguments = Bundle().apply {
                                        putInt(TAG_POSITION, position)
                                }
                        }
                }
        }
}
