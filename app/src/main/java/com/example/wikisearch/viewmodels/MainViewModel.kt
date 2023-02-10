package com.example.wikisearch.viewmodels

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment
import com.example.wikisearch.models.ServiceResponse
import com.example.wikisearch.repository.WikiRepository

class MainViewModel(private val repository: WikiRepository) : ViewModel() {
    private val query = MutableLiveData<String>()
    val wikiLiveData: LiveData<ServiceResponse> = Transformations.switchMap(query, ::getWikiDataFromRepo)
    val wikiClickLiveData = MutableLiveData<String?>()

    private fun getWikiDataFromRepo(text: String) = repository.getWikiResponse(text)

    fun getWikiSearch(text: String) = apply { query.value = text }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: WikiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }

    fun wikiItemClick(title:String){
        Log.d("WikiCLick", "wikiItemClicked: ")
        wikiClickLiveData.postValue(title)
    }
}