package com.bing.stockhelper.stock.list

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.adorkable.iosdialog.AlertDialog
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.databinding.ActivityStockListBinding
import com.bing.stockhelper.databinding.ItemStockBinding
import com.bing.stockhelper.model.entity.StockDetail
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.bing.stockhelper.stock.edit.StockEditActivity
import com.bing.stockhelper.utils.Constant
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import org.jetbrains.anko.startActivity

class StockListActivity : AppCompatActivity(), View.OnClickListener {
        private lateinit var binding: ActivityStockListBinding
        private lateinit var viewModel: StockListViewModel
        private lateinit var mAdapter: SimpleAdapter<StockDetail, ItemStockBinding>

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_list)
                viewModel = ViewModelProviders.of(this).get(StockListViewModel::class.java)

                initView()
        }

        private fun initView() {
                with(binding.refreshLayout) {
                        setEnableRefresh(false)
                        setEnableLoadMore(false)
                        setEnableOverScrollDrag(true)//是否启用越界拖动
                }
                with(binding.recyclerView) {
                        layoutManager = LinearLayoutManager(this@StockListActivity)
                        itemAnimator = DefaultItemAnimator()
                }
                initAdapter()
                viewModel.stocksLive.observe(this, Observer {
                        mAdapter.update(it)
                })

                addClickableViews(
                        binding.back
                )
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        onClick = {
                                startActivity<StockEditActivity>(Constant.TAG_STOCK_ID to it.id)
                        },
                        isSame = { old, newI -> old.isSameWith(newI) },
                        itemLayout = R.layout.item_stock,
                        bindData = { item, binding ->
                                binding.item = item
                                binding.flTags.text = item.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                binding.slTags.text = item.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                binding.root.setOnLongClickListener {
                                        AlertDialog(this)
                                                .init()
                                                .setMsg(getString(R.string.confirm_delete))
                                                .setPositiveButton("") {
                                                        viewModel.delete(item)
                                                }.setNegativeButton("") {

                                                }.show()
                                        true
                                }
                        }
                )
                binding.recyclerView.adapter = mAdapter
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> finish()
                }
        }
}
