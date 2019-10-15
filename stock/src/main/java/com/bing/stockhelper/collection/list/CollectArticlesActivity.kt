package com.bing.stockhelper.collection.list

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adorkable.iosdialog.AlertDialog
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.collection.detail.article.CollectArticleActivity
import com.bing.stockhelper.collection.detail.image.CollectImageActivity
import com.bing.stockhelper.databinding.ActivityCollectArticlesBinding
import com.bing.stockhelper.databinding.ItemCollectArticleBinding
import com.bing.stockhelper.kotlinexpands.initSlideBack
import com.bing.stockhelper.model.entity.*
import com.bing.stockhelper.utils.Constant
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import com.fanhantech.bottomdialog.BottomDialog
import org.jetbrains.anko.startActivity

class CollectArticlesActivity : AppCompatActivity(), View.OnClickListener {
        private lateinit var binding: ActivityCollectArticlesBinding
        private lateinit var viewModel: CollectArticlesViewModel
        private lateinit var mAdapter: SimpleAdapter<CollectArticle, ItemCollectArticleBinding>

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_collect_articles)
                initSlideBack()

                viewModel = ViewModelProviders.of(this).get(CollectArticlesViewModel::class.java)

                initView()
        }

        private fun initView() {
                with(binding.refreshLayout) {
                        setEnableRefresh(false)
                        setEnableLoadMore(false)
                        setEnableOverScrollDrag(true)//是否启用越界拖动
                }
                with(binding.recyclerView) {
                        layoutManager = GridLayoutManager(this@CollectArticlesActivity, 2)
                        itemAnimator = DefaultItemAnimator()
                }
                initAdapter()
                viewModel.stockTags.observe(this, Observer {
                        viewModel.separateTags(it)
                })
                viewModel.collectArticlesLive.observe(this, Observer {
                        mAdapter.update(it)
                })

                addClickableViews(
                        binding.back,
                        binding.fabAdd
                )
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        onClick = {
                                println("-------${it.type}")
                                when (it.type) {
                                        COLLECTION_TYPE_IMAGE -> startActivity<CollectImageActivity>(Constant.TAG_COLLECT_ARTICLE_ID to it.id)
                                        COLLECTION_TYPE_ARTICLE -> startActivity<CollectArticleActivity>(Constant.TAG_COLLECT_ARTICLE_ID to it.id)
                                }
                        },
                        isSame = { old, newI -> old.isSameWith(newI) },
                        itemLayout = R.layout.item_collect_article,
                        bindData = { item, binding ->
                                binding.item = item
                                binding.flTags.text = item.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                binding.slTags.text = item.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                binding.tvArticle.visibility = if (item.type == COLLECTION_TYPE_ARTICLE) View.VISIBLE else View.GONE
                                binding.ivImage.visibility = if (item.type == COLLECTION_TYPE_IMAGE) View.VISIBLE else View.GONE
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
                        R.id.fabAdd -> addCollect()
                }
        }

        private fun addCollect() {
                var dialog: BottomDialog? = null
                dialog = BottomDialog.create(supportFragmentManager, R.layout.dialog_add_collect_article, {
                        it.findViewById<LinearLayout>(R.id.text).setOnClickListener {
                                dialog?.dismiss()
                                startActivity<CollectArticleActivity>()
                        }
                        it.findViewById<LinearLayout>(R.id.image).setOnClickListener {
                                dialog?.dismiss()
                                startActivity<CollectImageActivity>()
                        }
                })
                dialog.show()
        }
}
