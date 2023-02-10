package com.example.wikisearch.viewmodels

import androidx.lifecycle.*
import com.example.wikisearch.models.ServiceResponse
import com.example.wikisearch.repository.WikiRepository

class MainViewModel(private val repository: WikiRepository) : ViewModel() {
    private val query = MutableLiveData<String>()
    val wikiLiveData: LiveData<ServiceResponse> = Transformations.switchMap(query, ::getEpisodeFromRepo)

    private fun getEpisodeFromRepo(pageNo: String) = repository.getWikiResponse()

    fun getWikiSearch() = apply { query.value = "" }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: WikiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }
}