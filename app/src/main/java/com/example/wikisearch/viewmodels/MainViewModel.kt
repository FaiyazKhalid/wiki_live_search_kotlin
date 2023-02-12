package com.example.wikisearch.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wikisearch.repository.WikiRepository
import com.example.wikisearch.room.database.getDatabase
import com.example.wikisearch.room.entity.WikiRoomEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WikiRepository, @ApplicationContext appContext: Context) : ViewModel() {

    val wikiClickLiveData = MutableLiveData<String?>()
    var wikiRoomLiveData: LiveData<List<WikiRoomEntity>> = getDatabase(appContext).wikiDao().getAllDataSet()

    fun getWikiSearchResponse(text: String){
        repository.getWikiResponse(text)
    }

    fun wikiItemClick(title: String) {
        wikiClickLiveData.postValue(title)
    }

    fun deleteAllData() {
        repository.deleteAllData()
    }

}