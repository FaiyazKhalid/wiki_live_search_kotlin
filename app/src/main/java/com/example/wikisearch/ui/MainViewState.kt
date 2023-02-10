package com.example.wikisearch.ui

import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

class MainViewState : BaseObservable() {

    @Bindable
    val title = "Wiki Search Page"

    @Bindable
    var shrimmerVisibility = View.INVISIBLE

    @Bindable
    var emptyViewVisibility = View.VISIBLE

    @Bindable
    var recyclerViewVisibility = View.GONE

    @Bindable
    var errorMessageVisibility = View.GONE

    @Bindable
    var errorMessageText: String? = null

    var apiInProgress = false
        set(value) {
            field = value
            if (field) {
                shrimmerVisibility = View.VISIBLE
                recyclerViewVisibility = View.VISIBLE
                errorMessageVisibility = View.GONE
                emptyViewVisibility = View.GONE
            } else {
                recyclerViewVisibility = View.VISIBLE
                shrimmerVisibility = View.INVISIBLE
                errorMessageVisibility = View.GONE
                emptyViewVisibility = View.GONE
            }
            notifyChange()
        }

    var emptyDataRequest = true
        set(value) {
            field = value
            if (field) {
                shrimmerVisibility = View.INVISIBLE
                emptyViewVisibility = View.VISIBLE
                recyclerViewVisibility = View.GONE
                errorMessageVisibility = View.GONE
            } else {
                recyclerViewVisibility = View.VISIBLE
                shrimmerVisibility = View.INVISIBLE
                errorMessageVisibility = View.GONE
                emptyViewVisibility = View.GONE
            }
            notifyChange()
        }


    fun setError(errorText: String) {
        errorMessageVisibility = View.VISIBLE
        errorMessageText = errorText
        notifyChange()
    }

}