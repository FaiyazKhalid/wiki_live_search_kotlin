package com.example.wikisearch.ui

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
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
import com.google.android.material.snackbar.Snackbar


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private val repository: WikiRepositoryImpl by lazy {
        WikiRepositoryImpl(RetrofitFactory.service)
    }
    private lateinit var viewState: MainViewState
    private lateinit var viewModel: MainViewModel
    private lateinit var searchView: SearchView
    var handler: Handler = Handler(Looper.getMainLooper())
    var doubleBackToExitPressedOnce = false


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

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if(searchView.query.isNotBlank()){
                searchView.setQuery("", true)
            }else{
                if (doubleBackToExitPressedOnce) {
                    requireActivity().finish();
                }
                doubleBackToExitPressedOnce = true;
                Snackbar.make(requireView(), "Please click BACK again to exit", Snackbar.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    doubleBackToExitPressedOnce = false
                }, 2000)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewState = MainActivity.viewState
        binding.viewState = viewState
        searchView = requireActivity().findViewById(R.id.searchView)
        setUpSearch()
        viewModel.wikiLiveData.observe(viewLifecycleOwner) {
            viewState.emptyDataRequest= true
            if (it.isSuccess) {
                it.data?.query?.pages?.let { it1 ->  viewState.emptyDataRequest= false ; initRecyclerView(it1) }
            } else {
                viewState.emptyDataRequest= true
                viewState.setError(it.message)
            }

        }

        viewModel.wikiClickLiveData.observe(viewLifecycleOwner) {
            Log.d("WikiCLick", "wikiLivedata: ")
            it?.let {
                NavHostFragment.findNavController(this)
                    .navigate(SearchFragmentDirections.actionFirstFragmentToSecondFragment(it))
                viewModel.wikiClickLiveData.value = null
            }
        }
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
        searchEditText.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
        searchEditText.setHintTextColor(ContextCompat.getColor(requireActivity(), R.color.gray))
        val searchClose: ImageView =
            searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
        searchClose.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_ATOP)
        searchView.isIconified = false

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewState.apiInProgress = true
                viewModel.getWikiSearch(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed({viewState.apiInProgress = true; viewModel.getWikiSearch(newText) }, 500)
                return false
            }
        })

        searchView.setOnCloseListener {
            Log.i("SearchView:", "onClose")
            //searchView.onActionViewCollapsed()
            searchView.setQuery("", true)
            true
        }
    }

}