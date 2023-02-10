package com.example.wikisearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wikisearch.utils.SearchRecyclerAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.wikisearch.databinding.FragmentSearchBinding
import com.example.wikisearch.models.Page
import com.example.wikisearch.repository.WikiRepositoryImpl
import com.example.wikisearch.utils.RetrofitFactory
import com.example.wikisearch.viewmodels.MainViewModel
import java.util.ArrayList

class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val repository: WikiRepositoryImpl by lazy {
        WikiRepositoryImpl(RetrofitFactory.service)
    }
    private lateinit var viewState: MainViewState
    private lateinit var viewModel: MainViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            requireActivity(), MainViewModel.Factory(repository = repository)
        )[MainViewModel::class.java]

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewState = MainViewState()
        binding.viewState = viewState

        viewModel.wikiLiveData.observe(viewLifecycleOwner) {
            viewState.apiInProgress = false
            if (it.isSuccess) {
                it.data?.query?.pages?.let { it1 -> initRecyclerView(it1) }
            } else {
                viewState.setError(it.message)
            }
        }
        viewModel.getWikiSearch()
    }

    private fun initRecyclerView(searchList: ArrayList<Page>) {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.hasFixedSize()
        val searchRecyclerAdapter = SearchRecyclerAdapter(searchList)
        recyclerView.adapter = searchRecyclerAdapter // set the Adapter to RecyclerView
    }

}