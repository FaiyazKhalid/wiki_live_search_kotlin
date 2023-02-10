package com.example.wikisearch.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.example.wikisearch.utils.CustomAdapter.MyViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.example.wikisearch.R
import com.bumptech.glide.Glide
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.example.wikisearch.models.Page
import com.example.wikisearch.ui.SearchFragmentDirections
import java.util.ArrayList

class CustomAdapter(var context: Context, var searchList: ArrayList<Page>, var fragment: Fragment) :
    RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // infalte the item Layout
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_item_layout, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return MyViewHolder(v)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // set the data in items
        val pos = holder.adapterPosition
        holder.name.text = searchList[pos].title
        holder.category.text = searchList[pos].terms?.description.toString();
        holder.category.isSelected = true
        loadImage(holder.image, searchList[pos].thumbnail?.source.toString())
        holder.itemView.setOnClickListener { // open another activity on item click
            NavHostFragment.findNavController(fragment)
                .navigate(
                    SearchFragmentDirections.actionFirstFragmentToSecondFragment(
                        searchList[pos].title.toString(),
                    )
                );
        }
    }

    private fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url) // image url
            .placeholder(R.drawable.app_logo) // any placeholder to load at start
            .error(R.drawable.app_logo) // any image in case of error
            .transition(withCrossFade())
            .centerCrop()
            .into(imageView)
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // init the item view's
        var name: TextView
        var category: TextView
        var image: ImageView

        init {
            // get the reference of item view's
            name = itemView.findViewById<View>(R.id.name) as TextView
            category = itemView.findViewById<View>(R.id.description) as TextView
            image = itemView.findViewById<View>(R.id.image) as ImageView
        }
    }
}