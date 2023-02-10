package com.example.wikisearch.ui

import android.annotation.SuppressLint
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
    lateinit var browser: WebView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            title = WebviewFragmentArgs.fromBundle(it).title.replace(" ", "_")
        }
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        browser = binding.webview
        browser.webViewClient = WebViewClient()
        browser.settings.javaScriptEnabled = true;
        browser.loadUrl(Constants.WEBVIEW_PAGE_URL + title);
    }
}