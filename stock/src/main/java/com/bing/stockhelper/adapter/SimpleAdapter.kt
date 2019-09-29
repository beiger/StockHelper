package com.bing.stockhelper.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

class SimpleAdapter<T, B: ViewDataBinding>(
        private var items: List<T>? = null,
        //TODO onClick最好不要用position，position只要不执行onBindView就永远不会更新
        private val onClick: ((t: T) -> Unit)? = null,
        private val isSame: (t1: T, t2: T) -> Boolean,
        private val itemLayout: Int = -1,
        private val bindData: (t: T, binding: B) -> Unit
): RecyclerView.Adapter<SimpleAdapter.ItemViewHolder<B>>() {

        fun update(_items: List<T>?) {
                _items ?: return
                if (items.isNullOrEmpty()) {
                        items = _items
                        notifyItemRangeInserted(0, _items.size)
                } else {
                        val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                                override fun getOldListSize(): Int {
                                        return items!!.size
                                }

                                override fun getNewListSize(): Int {
                                        return _items.size
                                }

                                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                                        return isSame(items!![oldItemPosition], _items[newItemPosition])
                                }

                                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                                        return isSame(items!![oldItemPosition], _items[newItemPosition])
                                }
                        })
                        items = _items
                        result.dispatchUpdatesTo(this)
                }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder<B> {
                val binding = DataBindingUtil.inflate<B>(LayoutInflater.from(parent.context), itemLayout, parent, false)
                return ItemViewHolder(binding)
        }

        override fun getItemCount(): Int {
                return items?.size ?: 0
        }

        override fun onBindViewHolder(holder: ItemViewHolder<B>, position: Int) {
                val item = items!![position]
                val binding = holder.binding
                onClick?.let {
                        binding.root.setOnClickListener {
                                it(item)
                        }
                }
                bindData(item, binding)
                binding.executePendingBindings()
        }


        class ItemViewHolder<B: ViewDataBinding>(val binding: B) : RecyclerView.ViewHolder(binding.root)
}