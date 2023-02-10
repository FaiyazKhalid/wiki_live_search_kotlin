package com.example.wikisearch.utils

import com.example.wikisearch.models.WikiModelRoot
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


/**
 * Created by Mayank Choudhary on 07-05-2021.
 * mayankchoudhary00@gmail.com
 */


interface ApiService {
    @GET("api.php")
    fun getWikiSearch(
        @Query("action") action: String="query",
        @Query("formatversion") formatVersion: String="2",
        @Query("prop") prop: String="pageimages|pageterms",
        @Query("format") format: String="json",
        @Query("generator") generator: String="prefixsearch",
        @Query("redirects") redirects: String = "1",
        @Query("piprop") piprop: String="thumbnail",
        @Query("pithumbsize") pithumbsize: String="50",
        @Query("pilimit") pilimit: String="10",
        @Query("wbptterms") wbptterms: String="description",
        @Query("gpssearch") gpssearch: String="Sachin T",
        @Query("gpslimit") gpslimit: String="10",
        //@Query("titles") titles: String?
    ): Call<WikiModelRoot>

    companion object {
        const val BASE_URL = Constants.BASE_URL
    }
}