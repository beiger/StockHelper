package com.bing.stockhelper.collection.display

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bing.stockhelper.R
import com.bing.stockhelper.collection.edit.ArticleEditActivity
import com.bing.stockhelper.databinding.ActivityArticleDisplayBinding
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import com.bing.stockhelper.utils.Constant
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import com.fanhantech.baselib.kotlinExpands.addClickableViews
import com.fanhantech.baselib.utils.UiUtil
import org.jetbrains.anko.startActivity

class ArticleDisplayActivity : AppCompatActivity(), View.OnClickListener {
        private lateinit var binding: ActivityArticleDisplayBinding
        private lateinit var viewModel: ArticleDisplayViewModel
        private var articleId: Int = 0

        override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                UiUtil.setBarColorAndFontBlack(this, Color.TRANSPARENT)
                binding = DataBindingUtil.setContentView(this, R.layout.activity_article_display)
                viewModel = ViewModelProviders.of(this).get(ArticleDisplayViewModel::class.java)

                intent?.let {
                        articleId = it.getIntExtra(Constant.TAG_COLLECT_ARTICLE_ID, -1)
                }
                ui {
                        waitIO {
                                viewModel.loadTags()
                        }
                        viewModel.load(articleId)
                        viewModel.articles.observe(this@ArticleDisplayActivity, Observer {
                                if (it.isNotEmpty()) {
                                        val item = it[0]
                                        binding.item = item
                                        binding.flTags.text = item.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                        binding.slTags.text = item.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                }
                        })
                        viewModel.stockTagLive.observe(this@ArticleDisplayActivity, Observer { tags ->
                                viewModel.stockTagsFirst = tags.filter { it.level == TAG_LEVEL_FIRST }
                                viewModel.stockTagsSecond = tags.filter { it.level == TAG_LEVEL_SECOND }
                        })
                }
                addClickableViews(
                        binding.back,
                        binding.ivEdit
                )
        }

        override fun onClick(v: View) {
                when (v.id) {
                        R.id.back -> finish()

                        R.id.ivEdit ->  startActivity<ArticleEditActivity>(Constant.TAG_COLLECT_ARTICLE_ID to articleId)
                }
        }
}
