package com.example.wikisearch.ui

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wikisearch.R
import com.example.wikisearch.databinding.FragmentSearchBinding
import com.example.wikisearch.models.Page
import com.example.wikisearch.repository.WikiRepositoryImpl
import com.example.wikisearch.utils.RetrofitFactory
import com.example.wikisearch.utils.SearchRecyclerAdapter
import com.example.wikisearch.viewmodels.MainViewModel


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val repository: WikiRepositoryImpl by lazy {
        WikiRepositoryImpl(RetrofitFactory.service)
    }
    private lateinit var viewState: MainViewState
    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
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
        searchView = requireActivity().findViewById(R.id.searchView)
        setUpSearch()
        viewModel.wikiLiveData.observe(viewLifecycleOwner) {
            viewState.apiInProgress = false
            if (it.isSuccess) {
                it.data?.query?.pages?.let { it1 -> initRecyclerView(it1) }
            } else {
                viewState.setError(it.message)
            }
        }

        viewModel.wikiClickLiveData.observe(viewLifecycleOwner){
            Log.d("WikiCLick", "wikiLivedata: ")
            it?.let {
                NavHostFragment.findNavController(this).navigate(SearchFragmentDirections.actionFirstFragmentToSecondFragment(it))
                viewModel.wikiClickLiveData.value = null
            }
        }

        viewModel.getWikiSearch()
    }

    private fun initRecyclerView(searchList: ArrayList<Page>) {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.hasFixedSize()
        val searchRecyclerAdapter = SearchRecyclerAdapter(searchList, viewModel)
        recyclerView.adapter = searchRecyclerAdapter // set the Adapter to RecyclerView
    }

    private fun setUpSearch() {
        searchView.visibility = View.VISIBLE
        val searchEditText =
            searchView.findViewById<View>(androidx.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(resources.getColor(R.color.black))
        searchEditText.setHintTextColor(resources.getColor(R.color.gray))
        val searchClose: ImageView =
            searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchClose.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        searchView.isIconified = false
        searchView.setOnCloseListener {
            Log.i("SearchView:", "onClose")
            //searchView.onActionViewCollapsed()
            searchView.setQuery("",true)
            true
        }
    }

}