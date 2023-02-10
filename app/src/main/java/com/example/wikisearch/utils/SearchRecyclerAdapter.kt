package com.example.wikisearch.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wikisearch.databinding.RowItemLayoutBinding
import com.example.wikisearch.models.Page
import com.example.wikisearch.utils.SearchRecyclerAdapter.MyViewHolder

class SearchRecyclerAdapter(
    var searchList: ArrayList<Page>,
) :
    RecyclerView.Adapter<MyViewHolder>() {
    private lateinit var binding: RowItemLayoutBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = RowItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(searchList[holder.adapterPosition])
    }

    override fun getItemCount(): Int {
        return searchList.size
    }

    class MyViewHolder(
        private val binding: RowItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wikiItem: Page) {
            binding.wikiData = wikiItem
        }
    }
}