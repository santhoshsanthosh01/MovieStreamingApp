package com.example.moviestreamingapp.request

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.moviestreamingapp.request.ApiInterface
import com.example.moviestreamingapp.request.ApiClient
import com.example.moviestreamingapp.utils.Constants

object ApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val movieApi = retrofit.create(ApiInterface::class.java)
}