package com.bing.stockhelper.main.follow

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
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
import com.fanhantech.baselib.app.ui
import com.fanhantech.baselib.app.waitIO
import org.jetbrains.anko.support.v4.startActivityForResult

class FollowFragment : Fragment() {
        private val REQUEST_CODE_EDIT = 0X00

        private lateinit var mBinding: FragmentFollowsBinding
        private lateinit var viewModel: FollowViewModel

        private lateinit var mAdapter: SimpleAdapter<ItemFollow.Info, ItemFollowBinding>

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                  savedInstanceState: Bundle?): View? {
                mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_follows, container, false)
                viewModel = ViewModelProviders.of(this).get(FollowViewModel::class.java)

                initRecyclerView()
                return mBinding.root
        }

        private fun initAdapter() {
                mAdapter = SimpleAdapter(
                        items = viewModel.followInfos,
                        onClick = { item, _ ->
                                startActivityForResult<FollowEditActivity>(REQUEST_CODE_EDIT, Constant.TAG_ITEM_FOLLOW to item)
                        },
                        isSame = { old, newI -> old.isSameWith(newI) },
                        itemLayout = R.layout.item_follow,
                        bindData = { item, position, binding ->
                                binding.item = item
                                binding.root.setOnLongClickListener {
                                        AlertDialog(context!!)
                                                .init()
                                                .setMsg(getString(R.string.confirm_delete))
                                                .setPositiveButton("") {
                                                        viewModel.delete(viewModel.follows[position])
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
                        ui {
                                waitIO { viewModel.loadFollows() }
                                initAdapter()
                        }
                }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
                super.onActivityResult(requestCode, resultCode, data)
                if (resultCode == Activity.RESULT_OK) {
                        when (requestCode) {
                                REQUEST_CODE_EDIT -> {
                                        ui {
                                                waitIO { viewModel.loadFollows() }
                                                mAdapter.update(viewModel.followInfos)
                                        }
                                }
                        }
                }
        }
}
