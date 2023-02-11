package com.example.wikisearch.viewmodels

import androidx.lifecycle.*
import com.example.wikisearch.WikiApp
import com.example.wikisearch.repository.WikiRepository
import com.example.wikisearch.room.database.getDatabase
import com.example.wikisearch.room.entity.WikiRoomEntity

class MainViewModel(private val repository: WikiRepository) : ViewModel() {
    //private val query = MutableLiveData<String>()
    /*val wikiLiveData: LiveData<List<WikiRoomEntity>?> =
        Transformations.switchMap(query, ::getWikiDataFromRepo)*/

    val wikiClickLiveData = MutableLiveData<String?>()
    var wikiRoomLiveData: LiveData<List<WikiRoomEntity>> = getDatabase(WikiApp.appContext).wikiDao().getAllDataSet()


    //private fun getWikiDataFromRepo(text: String) = repository.getWikiResponse(text)

    //fun getWikiSearch(text: String) = apply { query.value = text }

    fun getWikiSearchResponse(text: String){
        repository.getWikiResponse(text)
    }


    fun wikiItemClick(title: String) {
        wikiClickLiveData.postValue(title)
    }

    fun saveWikiData(wikiList: List<WikiRoomEntity>) {
        repository.insertRoomData(wikiList)
    }

    fun deleteAllData() {
        repository.deleteAllData()
    }

    @Suppress("UNCHECKED_CAST")
    class Factory(private val repository: WikiRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(repository) as T
        }
    }


}