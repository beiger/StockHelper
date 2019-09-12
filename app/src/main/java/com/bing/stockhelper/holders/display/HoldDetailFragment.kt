package com.bing.stockhelper.holders.display

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
import com.bing.stockhelper.utils.Constant
import org.jetbrains.anko.support.v4.startActivity

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
                val order = viewModel.ordersThisOpen[position]
                binding.order = order
                binding.cardView.setOnClickListener {
                        startActivity<HoldEditActivity>(Constant.TAG_ORDER_STATUS to order)
                }
                return binding.root
        }

        companion object {

                fun instance(position: Int): HoldDetailFragment {
                        return HoldDetailFragment().apply {
                                arguments = Bundle().apply {
                                        putInt(TAG_POSITION, position)
                                }
                        }
                }
        }
}
