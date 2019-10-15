package com.bing.stockhelper.main

import android.Manifest
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.files.folderChooser
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimplePagerAdapter
import com.bing.stockhelper.collection.list.CollectArticlesActivity
import com.bing.stockhelper.databinding.ActivityMainBinding
import com.bing.stockhelper.follow.FollowEditActivity
import com.bing.stockhelper.huawei.HuaweiActivity
import com.bing.stockhelper.holders.edit.HoldEditActivity
import com.bing.stockhelper.main.follow.FollowFragment
import com.bing.stockhelper.main.holder.HoldFragment
import com.bing.stockhelper.main.summary.SummaryFragment
import com.bing.stockhelper.search.SearchActivity
import com.bing.stockhelper.stock.list.StockListActivity
import com.bing.stockhelper.summary.SummaryEditActivity
import com.bing.stockhelper.tag.TagListActivity
import com.bing.stockhelper.widget.CustomTabLayout
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import org.jetbrains.anko.startActivity
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

        private lateinit var mBinding: ActivityMainBinding
        private lateinit var viewModel: MainViewModel
        private lateinit var mViewPager: ViewPager
        private lateinit var mTabs: CustomTabLayout
        private lateinit var mFragmentPagerAdapter: FragmentPagerAdapter
        private var currentPosition = 0

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
                viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
                initView()
        }

        private fun initView() {
                val indicator = DrawerArrowDrawable(this)
                indicator.color = getColor(R.color.bg_black_cc)
                mBinding.indicator.setImageDrawable(indicator)

                with(mBinding.drawerLayout) {
                        setScrimColor(Color.TRANSPARENT)
                        addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
                                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                                        indicator.progress = slideOffset
                                        val content = getChildAt(0)
                                        content.translationX = drawerView.measuredWidth * slideOffset
                                }
                        })
                }

                mViewPager = findViewById(R.id.viewpager)
                val fragments = ArrayList<Fragment>()
                fragments.add(HoldFragment())
                fragments.add(FollowFragment())
                fragments.add(SummaryFragment())

                val titles = mutableListOf(
                        getString(R.string.hold),
                        getString(R.string.follow),
                        getString(R.string.summary)
                )
                mFragmentPagerAdapter = SimplePagerAdapter(supportFragmentManager, fragments, titles)
                mViewPager.adapter = mFragmentPagerAdapter
                mViewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener{
                        override fun onPageScrollStateChanged(state: Int) {  }

                        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

                        override fun onPageSelected(position: Int) {
                                currentPosition = position
                        }
                })

                mTabs = findViewById(R.id.tabs)
                mTabs.setupWithViewPager(mViewPager)

                addClickableViews(
                        mBinding.indicator,
                        mBinding.search,
                        mBinding.fabAdd,
                        mBinding.tvHuawei,
                        mBinding.tvAllStocks,
                        mBinding.tvCollections,
                        mBinding.tvTags,
                        mBinding.tvBackup,
                        mBinding.tvAddFromFile
                )
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.indicator -> mBinding.drawerLayout.openDrawer(Gravity.LEFT)
                        R.id.search -> {
                                startActivity<SearchActivity>()
                                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                        R.id.fabAdd -> {
                                when (currentPosition) {
                                        0 -> startActivity<HoldEditActivity>()
                                        1 -> startActivity<FollowEditActivity>()
                                        2 -> startActivity<SummaryEditActivity>()
                                }
                        }
                        R.id.tvHuawei -> startActivity<HuaweiActivity>()
                        R.id.tvAllStocks -> startActivity<StockListActivity>()
                        R.id.tvCollections -> startActivity<CollectArticlesActivity>()
                        R.id.tvTags -> startActivity<TagListActivity>()
                        R.id.tvBackup -> viewModel.backup()
                        R.id.tvAddFromFile -> {
                                RxPermissions(this)
                                        .request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe {
                                                if (it) {
                                                        MaterialDialog(this).show {
                                                                folderChooser { _, file ->
                                                                        viewModel.recoverFromFile(file.absolutePath)
                                                                }
                                                        }
                                                }
                                        }
                        }
                }
        }

        fun hideFab(hide: Boolean) {
                if (hide) {
                        mBinding.fabAdd.hide()
                } else {
                        mBinding.fabAdd.show()
                }
        }

        override fun onBackPressed() {
                if (mBinding.drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                        mBinding.drawerLayout.closeDrawer(Gravity.LEFT)
                } else {
                        super.onBackPressed()
                }
        }
}
