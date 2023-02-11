package com.example.wikisearch.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wikisearch.WikiApp
import com.example.wikisearch.models.ServiceResponse
import com.example.wikisearch.models.WikiModelRoot
import com.example.wikisearch.room.database.getDatabase
import com.example.wikisearch.room.entity.WikiRoomEntity
import com.example.wikisearch.utils.ApiService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class WikiRepositoryImpl(private val apiService: ApiService) : WikiRepository {
    private val _liveData: MutableLiveData<ServiceResponse> by lazy {
        MutableLiveData<ServiceResponse>()
    }

    override fun getWikiResponse(text: String): LiveData<ServiceResponse> {
        apiService.getWikiSearch(gpssearch = text).enqueue(object : Callback<WikiModelRoot> {
            override fun onFailure(call: Call<WikiModelRoot>, t: Throwable) {
                _liveData.value = ServiceResponse(isSuccess = false, message = t.localizedMessage)
            }

            override fun onResponse(
                call: Call<WikiModelRoot>,
                response: Response<WikiModelRoot>
            ) {
                val wikiData: ArrayList<WikiRoomEntity> = ArrayList()
                response.body()?.query?.pages?.let { it1 ->
                    it1.map { it2 ->
                        wikiData.add(
                            WikiRoomEntity(
                                it2.pageid,
                                it2.title,
                                it2.thumbnail?.source,
                                it2.terms?.description.toString()
                                    .replace("""[\[\]]""".toRegex(), "")
                            )
                        )
                    }
                    insertRoomData(wikiData)
                }

                _liveData.value = ServiceResponse(
                    isSuccess = response.code() == 200,
                    message = response.message(), data = response.body(),
                    code = response.code()
                )
            }
        })
        return _liveData
    }

    override fun insertRoomData(wikiRoomData: List<WikiRoomEntity>) {
        val wikiIds: ArrayList<Int> = ArrayList()
        wikiRoomData.map {
            wikiIds.add(it.pageId)
        }
        GlobalScope.launch {
            getDatabase(WikiApp.appContext).wikiDao().insertAll(wikiRoomData)
            getDatabase(WikiApp.appContext).wikiDao().deleteOldUsers(wikiIds)
        }
    }

    override fun deleteAllData() {
        GlobalScope.launch {
            getDatabase(WikiApp.appContext).wikiDao().deleteAll()
        }

    }
}


interface WikiRepository {
    fun getWikiResponse(text: String): LiveData<ServiceResponse>
    fun insertRoomData(wikiRoomData: List<WikiRoomEntity>)
    fun deleteAllData()
}