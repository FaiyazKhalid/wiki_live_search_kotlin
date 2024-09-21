package com.advocatepedia.lite.ui

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.advocatepedia.lite.R
import com.advocatepedia.lite.databinding.FragmentWebviewBinding
import com.advocatepedia.lite.utils.Constants
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class WebviewFragment : Fragment() {

    private lateinit var binding: FragmentWebviewBinding
    private var title: String? = null
    lateinit var browser: WebView
    private lateinit var searchView: SearchView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        arguments?.let {
            title = WebviewFragmentArgs.fromBundle(it).title.replace(" ", "_")
        }
        binding = FragmentWebviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchView = requireActivity().findViewById(R.id.searchView)
        searchView.visibility=View.GONE
        browser = binding.webview
        browser.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }
            override fun onPageFinished(view: WebView, url: String) {
                binding.startAnimation.visibility=View.GONE
                super.onPageFinished(view, url)
            }
        }
        browser.settings.javaScriptEnabled = true;
        browser.loadUrl(Constants.WEBVIEW_PAGE_URL + title);

    }
}