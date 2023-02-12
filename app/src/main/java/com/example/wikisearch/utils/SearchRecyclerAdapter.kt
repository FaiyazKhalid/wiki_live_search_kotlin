package com.example.wikisearch.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.wikisearch.databinding.RowItemLayoutBinding
import com.example.wikisearch.room.entity.WikiRoomEntity
import com.example.wikisearch.utils.SearchRecyclerAdapter.MyViewHolder
import com.example.wikisearch.viewmodels.MainViewModel

class SearchRecyclerAdapter(
    var searchList: ArrayList<WikiRoomEntity>?,
    private val viewModel:MainViewModel
) :
    RecyclerView.Adapter<MyViewHolder>() {
    private lateinit var binding: RowItemLayoutBinding


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = RowItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        binding.viewModel = viewModel
        holder.bind(searchList?.get(holder.adapterPosition) ?:WikiRoomEntity(0,"","","") )
    }

    override fun getItemCount(): Int {
        return searchList?.size ?:0
    }

    fun updateData(searchListx: ArrayList<WikiRoomEntity>?){
        searchList = searchListx
        notifyDataSetChanged()
    }

    class MyViewHolder(
        private val binding: RowItemLayoutBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wikiItem: WikiRoomEntity) {
            binding.wikiData = wikiItem
            binding.description.isSelected = true
        }
    }
}