package com.bing.stockhelper.search

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bing.stockhelper.R
import com.bing.stockhelper.databinding.ActivitySearchBinding
import com.bing.stockhelper.kotlinexpands.initSlideBack
import com.fanhantech.baselib.kotlinExpands.afterTextChanged
import com.fanhantech.baselib.utils.UiUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class SearchActivity : AppCompatActivity(), View.OnClickListener {
        private lateinit var mBinding: ActivitySearchBinding
        private val compositeDisposable = CompositeDisposable()

        private lateinit var mRecyclerView: RecyclerView

        private val searchSubject = PublishSubject.create<String>()
        private val mSearchText = ObservableField("")

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                mBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
                initSlideBack()

                with(mBinding) {
                        searchText = mSearchText
                        back.setOnClickListener(this@SearchActivity)
                }

                compositeDisposable.add(
                        searchSubject
                                .debounce(400, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe {
                                        updateAdapter(it)
                                }
                )

                // 从数据库中获取数据


                initRecycleView()
                initSearchView()
        }

        private fun initRecycleView() {
                mRecyclerView = mBinding.recyclerView
                mRecyclerView.layoutManager = LinearLayoutManager(this)
                mRecyclerView.itemAnimator = DefaultItemAnimator()
        }

        private fun initSearchView() {
                mBinding.searchView.afterTextChanged {
                        searchSubject.onNext(it.toString())
                        mSearchText.set(if (it.toString() == "") "" else "\"" + it.toString() + "\"")
                }
        }

        private fun updateAdapter(text: String) {

        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> finish()

                        else -> {
                        }
                }
        }

        override fun finish() {
                super.finish()
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }

        override fun onDestroy() {
                compositeDisposable.dispose()
                super.onDestroy()
        }
}
