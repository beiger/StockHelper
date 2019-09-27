package com.bing.stockhelper.stock

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.databinding.ActivityStockListBinding
import com.bing.stockhelper.databinding.ItemStockSearchBinding
import com.bing.stockhelper.model.AppDatabase
import com.bing.stockhelper.model.entity.StockDetail
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.kotlinExpands.afterTextChanged
import com.fanhantech.baselib.utils.UiUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.startActivity
import java.util.concurrent.TimeUnit

class StockListActivity : AppCompatActivity(), View.OnClickListener {
        private lateinit var binding: ActivityStockListBinding
        private lateinit var database: AppDatabase

        private lateinit var stocksLive: LiveData<List<StockDetail>>
        private val filterStockLive = MutableLiveData<List<StockDetail>>()
        private lateinit var adapter: SimpleAdapter<StockDetail, ItemStockSearchBinding>

        private val searchSubject = PublishSubject.create<String>()
        private val compositeDisposable = CompositeDisposable()

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_stock_list)

                database = AppDatabase.getInstance(application)
                stocksLive = database.loadStocksLive()
                compositeDisposable.add(
                        searchSubject
                                .debounce(400, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                        filterStock(it)
                                }
                )

                addClickableViews(
                        binding.back,
                        binding.ivAdd
                )

                initRecycleView()
                initSearchView()

                stocksLive.observe(this, Observer {
                        searchSubject.onNext(binding.searchView.text.toString())
                })
                filterStockLive.observe(this, Observer {
                        adapter.update(it)
                })
        }

        private fun initRecycleView() {
                adapter = SimpleAdapter(
                        items = null,
                        onClick = { item, position ->

                        },
                        isSame = { old, newItem ->
                                old.isSameWith(newItem)
                        },
                        itemLayout = R.layout.item_stock_search,
                        bindData = { item, _, binding ->
                                binding.stock = item
                        }
                )
                with(binding.recyclerView) {
                        layoutManager = GridLayoutManager(this@StockListActivity, 2)
                        itemAnimator = DefaultItemAnimator()
                }
        }

        private fun initSearchView() {
                binding.searchView.afterTextChanged {
                        searchSubject.onNext(it.toString())
                }
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> finish()
                        R.id.ivAdd -> startActivity<StockEditActivity>()
                }
        }

        private fun filterStock(text: String) {
                val all = stocksLive.value ?: return
                filterStockLive.value = all.filter {
                        it.name.contains(text) || it.code.contains(text)
                }
        }

        override fun onDestroy() {
                compositeDisposable.dispose()
                super.onDestroy()
        }
}