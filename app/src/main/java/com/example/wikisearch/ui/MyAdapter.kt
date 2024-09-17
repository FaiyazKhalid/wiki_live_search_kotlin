package com.example.wikisearch.ui

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.example.wikisearch.R
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class MyAdapter(val context : Activity, val productArrayList: List<Product>) :
RecyclerView.Adapter<MyAdapter.MyViewHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
val itemView = LayoutInflater.from(context).inflate(R.layout.eachitem, parent, false)
        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int {
        return productArrayList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = productArrayList[position]
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
    /*    holder.otd.text = currentItem.otd
        holder.wish.text = currentItem.wish
        holder.news.text = currentItem.news*/

        Picasso.get().load(currentItem.thumbnail).into(holder.image)
    }
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        var title : TextView
        var image : ShapeableImageView
        var description: TextView
       /* var otd: TextView
        var wish: TextView
        var news: TextView*/

        init {
            title = itemView.findViewById(R.id.productTitle)
            image = itemView.findViewById(R.id.productImage)
            description = itemView.findViewById(R.id.description)


         /*   otd = itemView.findViewById(R.id.otd)
            wish = itemView.findViewById(R.id.wish)
            news = itemView.findViewById(R.id.news)*/

        }




    }
}


