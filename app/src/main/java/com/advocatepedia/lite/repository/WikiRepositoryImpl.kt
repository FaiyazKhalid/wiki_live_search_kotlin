package com.advocatepedia.lite.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.advocatepedia.lite.models.ServiceResponse
import com.advocatepedia.lite.models.WikiModelRoot
import com.advocatepedia.lite.room.database.getDatabase
import com.advocatepedia.lite.room.entity.WikiRoomEntity
import com.advocatepedia.lite.utils.ApiService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WikiRepositoryImpl @Inject constructor(private val apiService: ApiService, @ApplicationContext private val context: Context) : WikiRepository {
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
                }
                if(wikiData.size<=0){
                    wikiData.add(
                        WikiRoomEntity(
                            0,
                           "No Match Found",
                            "",
                           "Try with other keywords"
                        )
                    )
                }
                insertRoomData(wikiData)

                _liveData.value = ServiceResponse(
                    isSuccess = response.code() == 200,
                    message = response.message(), data = response.body(),
                    code = response.code()
                )
            }
        })
        return _liveData
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun insertRoomData(wikiRoomData: List<WikiRoomEntity>) {
        val wikiIds: ArrayList<Int> = ArrayList()
        wikiRoomData.map {
            wikiIds.add(it.pageId)
        }
        GlobalScope.launch {
            getDatabase(context).wikiDao().insertAll(wikiRoomData)
            getDatabase(context).wikiDao().deleteOldUsers(wikiIds)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun deleteAllData() {
        GlobalScope.launch {
            getDatabase(context).wikiDao().deleteAll()
        }

    }
}


interface WikiRepository {
    fun getWikiResponse(text: String): LiveData<ServiceResponse>
    fun insertRoomData(wikiRoomData: List<WikiRoomEntity>)
    fun deleteAllData()
}