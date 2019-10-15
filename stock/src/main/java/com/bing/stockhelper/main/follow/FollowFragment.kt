package com.bing.stockhelper.main.follow

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.adorkable.iosdialog.AlertDialog
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.databinding.FragmentFollowsBinding
import com.bing.stockhelper.databinding.ItemFollowBinding
import com.bing.stockhelper.follow.FollowEditActivity
import com.bing.stockhelper.model.entity.ItemFollow
import com.bing.stockhelper.model.entity.TAG_LEVEL_FIRST
import com.bing.stockhelper.model.entity.TAG_LEVEL_SECOND
import org.jetbrains.anko.support.v4.startActivity

class FollowFragment : Fragment() {

        private lateinit var mBinding: FragmentFollowsBinding
        private lateinit var viewModel: FollowViewModel

        private lateinit var mAdapter: SimpleAdapter<ItemFollow.Info, ItemFollowBinding>

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
                mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_follows, container, false)
                viewModel = ViewModelProviders.of(this).get(FollowViewModel::class.java)

                initRecyclerView()

                viewModel.stockTags.observe(this, Observer {
                        viewModel.separateTags(it)
                })
                viewModel.followInfos.observe(this, Observer {
                        mAdapter.update(it)
                })
                return mBinding.root
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        onClick = { item ->
                                startActivity<FollowEditActivity>(Constant.TAG_ITEM_FOLLOW_ID to item.id)
                        },
                        isSame = { old, newI -> old.isSameWith(newI) },
                        itemLayout = R.layout.item_follow,
                        bindData = { item, binding ->
                                binding.item = item
                                binding.flTags.text = item.tagsStr(TAG_LEVEL_FIRST, viewModel.stockTagsFirst)
                                binding.slTags.text = item.tagsStr(TAG_LEVEL_SECOND, viewModel.stockTagsSecond)
                                binding.rate.starProgress = item.focusDegree * 10f
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

        private fun initRecyclerView() {
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
        }
}
