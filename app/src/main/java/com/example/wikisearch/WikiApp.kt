package com.example.wikisearch

import android.app.Application
import android.content.Context


class WikiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    companion object {
        private lateinit var context: Context
        val appContext: Context
            get() = context
    }
}