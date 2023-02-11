package com.example.wikisearch.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.wikisearch.R

class ViewBindingAdapter {
    companion object {
        @JvmStatic @BindingAdapter("imageUrl")
        fun loadImage(view: ImageView, url: String?) {
            Glide.with(view.context)
                .load(url) // image url
                .placeholder(R.drawable.wiki_logo) // any placeholder to load at start
                .error(R.drawable.wiki_logo) // any image in case of error
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(view)
        }

        @JvmStatic @BindingAdapter("textArray")
        fun loadText(view: TextView, array: List<String>?) {
            view.text = array.toString().replace("""[\[\]]""".toRegex(),"")
            view.isSelected = true
        }
    }
}