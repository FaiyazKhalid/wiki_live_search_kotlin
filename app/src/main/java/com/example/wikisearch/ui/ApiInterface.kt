package com.example.wikisearch.ui

import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("product.php")
    fun getProductData() : Call<MyData>
}