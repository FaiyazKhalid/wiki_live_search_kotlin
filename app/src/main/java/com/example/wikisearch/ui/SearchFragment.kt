package com.example.wikisearch.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wikisearch.R
import com.example.wikisearch.databinding.FragmentSearchBinding
import com.example.wikisearch.room.entity.WikiRoomEntity
import com.example.wikisearch.utils.SearchRecyclerAdapter
import com.example.wikisearch.utils.VoiceRecognition
import com.example.wikisearch.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import net.gotev.speech.Speech
import net.gotev.speech.ui.SpeechProgressView


@AndroidEntryPoint
class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var viewState: MainViewState
    private val viewModel: MainViewModel by viewModels()
    private lateinit var searchView: SearchView
    var handler: Handler = Handler(Looper.getMainLooper())
    var doubleBackToExitPressedOnce = false
    lateinit var searchRecyclerAdapter: SearchRecyclerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (doubleBackToExitPressedOnce) {
                requireActivity().finish();
            }
            doubleBackToExitPressedOnce = true;
            Snackbar.make(requireView(), "Please click BACK again to exit", Snackbar.LENGTH_SHORT)
                .show()
            Handler(Looper.getMainLooper()).postDelayed({
                doubleBackToExitPressedOnce = false
            }, 2000)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewState = MainActivity.viewState
        binding.viewState = viewState
        searchView = requireActivity().findViewById(R.id.searchView)
        setUpSearch()
        initRecyclerView()
        setUpVoiceSearch()
        Speech.init(requireContext(), activity?.packageName);

        viewModel.wikiRoomLiveData.observe(viewLifecycleOwner) {
            viewState.emptyDataRequest = it.isNullOrEmpty()
            updateData(it as ArrayList<WikiRoomEntity>?)
        }

        viewModel.wikiClickLiveData.observe(viewLifecycleOwner) {
            it?.let {
                NavHostFragment.findNavController(this)
                    .navigate(SearchFragmentDirections.actionFirstFragmentToSecondFragment(it))
                viewModel.wikiClickLiveData.value = null
            }
        }
    }

    private fun initRecyclerView() {
        val recyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.hasFixedSize()
        searchRecyclerAdapter = SearchRecyclerAdapter(null, viewModel)
        recyclerView.adapter = searchRecyclerAdapter // set the Adapter to RecyclerView
    }

    private fun updateData(searchList: ArrayList<WikiRoomEntity>?) {
        searchRecyclerAdapter.updateData(searchList)
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
                if (query.isNotBlank()) {
                    viewState.apiInProgress = true
                    viewModel.getWikiSearchResponse(query)
                } else {
                    viewModel.deleteAllData()
                }
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                handler.removeCallbacksAndMessages(null);
                if (newText.isNotBlank()) {
                    viewState.apiInProgress = true;
                    handler.postDelayed({
                        viewModel.getWikiSearchResponse(
                            newText
                        )
                    }, 500)
                } else {
                    viewModel.deleteAllData()
                }
                return false
            }
        })

        searchView.setOnCloseListener {
            //searchView.onActionViewCollapsed()
            viewModel.deleteAllData()
            viewState.emptyDataRequest = true
            true
        }
    }


    private fun setUpVoiceSearch() {
        val  speechProgressView = requireActivity().findViewById<SpeechProgressView>(R.id.progress_speech)
        val heights = intArrayOf(30, 38, 29, 40, 27)
        speechProgressView.setBarMaxHeightsInDp(heights);
        val microphone = requireActivity().findViewById<ImageView>(R.id.microphone)
        val voiceRecognition =
            VoiceRecognition(requireContext(), viewState, searchView, speechProgressView)

        microphone.setOnClickListener {
            when (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED) {
                true -> voiceRecognition.listenMic()
                false -> voiceRecognition.takeMicPermission()
            }
        }
    }


}