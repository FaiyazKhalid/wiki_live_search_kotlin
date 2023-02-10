package com.example.wikisearch.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.wikisearch.databinding.FragmentWebviewBinding
import com.example.wikisearch.utils.Constants


class WebviewFragment : Fragment() {

    private lateinit var binding: FragmentWebviewBinding
    private var title: String? = null
    lateinit var browser:WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            title = WebviewFragmentArgs.fromBundle(it).title.replace(" ","_")
        }

        Log.d("MovieDetails", "onCreateView: Title: $title")
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        browser=binding.webview
        browser.webViewClient = WebViewClient()
        browser.getSettings().setJavaScriptEnabled(true);
        browser.loadUrl(Constants.basePageUri+title);
    }
}