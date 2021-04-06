package com.pasaoglu.movieapp.ui.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

@BindingAdapter("imageFromUrl")
fun bindImageFromUrl(view: ImageView, imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .transform(CenterCrop(), RoundedCorners(3))
            .into(view)
    } else {
        Glide.with(view.context)
            .clear(view)
        view.setImageResource(android.R.drawable.presence_offline)
    }
}