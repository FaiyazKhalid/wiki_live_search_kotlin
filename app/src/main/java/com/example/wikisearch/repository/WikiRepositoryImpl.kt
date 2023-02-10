package com.example.wikisearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wikisearch.models.ServiceResponse
import com.example.wikisearch.models.WikiModelRoot
import com.example.wikisearch.utils.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WikiRepositoryImpl(private val apiService: ApiService) : WikiRepository {
    private val _liveData: MutableLiveData<ServiceResponse> by lazy {
        MutableLiveData<ServiceResponse>()
    }

    override fun getWikiResponse(text: String): LiveData<ServiceResponse> {
        apiService.getWikiSearch(gpssearch = text).enqueue(object : Callback<WikiModelRoot> {
            override fun onFailure(call: Call<WikiModelRoot>, t: Throwable) {
                _liveData.value = ServiceResponse(isSuccess = false, message = t.localizedMessage)
            }

            override fun onResponse(call: Call<WikiModelRoot>,
                                    response: Response<WikiModelRoot>
            ) {
                _liveData.value = ServiceResponse(isSuccess = response.code() == 200,
                    message = response.message(), data = response.body(),
                    code = response.code())
            }
        })
        return _liveData
    }
}

interface WikiRepository {
    fun getWikiResponse(text:String): LiveData<ServiceResponse>
}