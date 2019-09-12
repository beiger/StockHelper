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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.bing.stockhelper.utils.Constant
import com.bing.stockhelper.R
import com.bing.stockhelper.adapter.SimpleAdapter
import com.bing.stockhelper.databinding.FragmentFollowsBinding
import com.bing.stockhelper.databinding.ItemFollowBinding
import com.bing.stockhelper.follow.FollowEditActivity
import com.bing.stockhelper.model.entity.ItemFollow
import com.scwang.smartrefresh.layout.api.RefreshLayout
import org.jetbrains.anko.support.v4.startActivity

class FollowFragment : Fragment(){

        private lateinit var mBinding: FragmentFollowsBinding
        private lateinit var viewModel: FollowViewModel

        private lateinit var mRecyclerView: RecyclerView
        private lateinit var mRefreshLayout: RefreshLayout
        private lateinit var mAdapter: SimpleAdapter<ItemFollow, ItemFollowBinding>

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
                mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_follows, container, false)
                viewModel = ViewModelProviders.of(this).get(FollowViewModel::class.java)

                initRecyclerView()
                return mBinding.root
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        onClick = { item, _ ->
                                startActivity<FollowEditActivity>(Constant.TAG_ITEM_FOLLOW to item)
                        },
                        isSame = { old, newI -> old.code == newI.code },
                        itemLayout = R.layout.item_follow,
                        bindData = { item, binding ->
                                binding.item = item
                                binding.root.setOnLongClickListener {
                                        MaterialDialog(context!!).show {
                                                message(R.string.confirm_delete)
                                                positiveButton {
                                                        viewModel.delete(item)
                                                }
                                                negativeButton {  }
                                        }
                                        true
                                }
                        }
                )
                mRecyclerView.adapter = mAdapter
        }

        private fun initRecyclerView() {
                mRefreshLayout = mBinding.refreshLayout
                mRefreshLayout.setEnableRefresh(false)
                mRefreshLayout.setEnableLoadMore(false)
                mRefreshLayout.setEnableOverScrollDrag(true)//是否启用越界拖动

                mRecyclerView = mBinding.recyclerView
                mRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                mRecyclerView.itemAnimator = DefaultItemAnimator()
                initAdapter()

                viewModel.follows.observe(this, Observer{ mAdapter.update(it) })
        }
}
