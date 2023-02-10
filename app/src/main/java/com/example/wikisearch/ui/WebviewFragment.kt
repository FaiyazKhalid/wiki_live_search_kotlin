package com.example.wikisearch.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.wikisearch.R
import com.example.wikisearch.databinding.FragmentWebviewBinding

class WebviewFragment : Fragment() {
    private var binding: FragmentWebviewBinding? = null
    private var title: String? = null
    private var content: String? = null
    private var date: String? = null
    private var category: String? = null
    private var image: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            title = WebviewFragmentArgs.fromBundle(it).title
            category = WebviewFragmentArgs.fromBundle(it).category
            content = WebviewFragmentArgs.fromBundle(it).content
            image = WebviewFragmentArgs.fromBundle(it).image
            date = WebviewFragmentArgs.fromBundle(it).date
        }

        Log.d("MovieDetails", "onCreateView: Title: $title")
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateMovieUi()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    private fun updateMovieUi() {
        binding!!.textviewMovieDetails.text = content + content + content + content + content
        binding!!.title.text = title
        binding!!.date.text = date
        binding!!.category.text = category
        Glide.with(binding!!.movieDetailsPoster)
            .load(image) // image url
            .placeholder(R.drawable.no_picture) // any placeholder to load at start
            .error(R.drawable.no_picture) // any image in case of error
            .into(binding!!.movieDetailsPoster)
    }
}