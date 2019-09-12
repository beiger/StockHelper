package com.fanhantech.baselib.bindingadapter

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fanhantech.baselib.glide.GlideApp

@BindingAdapter("img_url")
fun showImg(iv: ImageView, url: String?) {
        Glide.with(iv.context).load(url).into(iv)
}

@BindingAdapter("img_url_no_cache")
fun showImgWithoutCache(iv: ImageView, url: String?) {
        GlideApp.with(iv.context).load(url).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(iv)
}

@BindingAdapter("visible")
fun isVisible(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
}