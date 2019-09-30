package com.bing.stockhelper.main.holder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adorkable.iosdialog.AlertDialog
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.FragmentHoldBinding
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.databinding.ItemOrderDetailBinding
import com.bing.stockhelper.holders.display.HoldsActivity
import com.bing.stockhelper.main.MainActivity
import com.bing.stockhelper.model.entity.DayAttention
import com.bing.stockhelper.model.entity.OrderDetail
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.blankj.utilcode.util.KeyboardUtils
import com.fanhantech.baselib.app.io
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import org.jetbrains.anko.support.v4.startActivity

class HoldFragment : Fragment(), View.OnClickListener {

        private lateinit var mBinding: FragmentHoldBinding
        private lateinit var viewModel: HoldViewModel

        private lateinit var mAdapter: SimpleAdapter<OrderDetail.DetailInfo, ItemOrderDetailBinding>

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
                mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_hold, container, false)
                viewModel = ViewModelProviders.of(this).get(HoldViewModel::class.java)

                initView()
                return mBinding.root
        }

        private fun initView() {
               with(mBinding.refreshLayout) {
                       setEnableRefresh(false)
                       setEnableLoadMore(false)
                       setEnableOverScrollDrag(true)//是否启用越界拖动
               }
                with(mBinding.recyclerView) {
                        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                        itemAnimator = DefaultItemAnimator()
                }
                initAdapter()
                viewModel.stockTags.observe(this, Observer {
                        viewModel.seperaterTags(it)
                })
                viewModel.orderDetailInfos.observe(this, Observer {
                        mAdapter.update(it)
                })

                viewModel.dayAttentions.observe(this, Observer{
                        if (mBinding.etAttention.text.isNotEmpty()) {
                                return@Observer
                        }
                        if (it.isNullOrEmpty()) {
                                mBinding.etAttention.setText("")
                        } else {
                                mBinding.etAttention.setText(it[0].content)
                        }
                })

                mBinding.panelLayout.addPanelSlideListener(object : SlidingUpPanelLayout.PanelSlideListener {
                        override fun onPanelSlide(panel: View?, slideOffset: Float) { }

                        override fun onPanelStateChanged(
                                panel: View,
                                previousState: SlidingUpPanelLayout.PanelState,
                                newState: SlidingUpPanelLayout.PanelState
                        ) {
                                (activity as MainActivity).hideFab(newState != SlidingUpPanelLayout.PanelState.COLLAPSED)
                        }
                })

                addClickableViews(
                        mBinding.ivEdit,
                        mBinding.ivCheck
                )
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        onClick = { item ->
                                startActivity<HoldsActivity>(Constant.TAG_ORDER_DETAIL_ID to item.id)
                        },
                        isSame = { old, newI -> old.isSameWith(newI) },
                        itemLayout = R.layout.item_order_detail,
                        bindData = { item, binding ->
                                binding.item = item
                                binding.flTags.text = item.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                binding.slTags.text = item.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                binding.root.setOnLongClickListener {
                                        AlertDialog(context!!)
                                                .init()
                                                .setMsg(getString(R.string.confirm_delete))
                                                .setPositiveButton("") {
                                                        viewModel.delete(item.id)
                                                }.setNegativeButton("") {

                                                }.show()
                                        true
                                }
                        }
                )
                mBinding.recyclerView.adapter = mAdapter
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.ivEdit -> enableEditText(mBinding.etAttention, true)

                        R.id.ivCheck -> {
                                KeyboardUtils.hideSoftInput(v)
                                enableEditText(mBinding.etAttention, false)
                                updateAttention()
                        }
                }
        }

        private fun enableEditText(et: EditText, enable: Boolean) {
                if (enable) {
                        et.isFocusableInTouchMode = true
                        et.isFocusable = true
                        et.requestFocus()
                } else {
                        et.isFocusable = false
                        et.isFocusableInTouchMode = false
                }
        }

        private fun updateAttention() {
                io {
                        viewModel.deleteAllAttention()
                        val content = mBinding.etAttention.text.toString()
                        if (content.isNotEmpty()) {
                                viewModel.insert(DayAttention(0, content))
                        }
                }
        }
}
