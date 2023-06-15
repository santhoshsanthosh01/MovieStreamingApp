package com.example.moviestreamingapp.repositary

import com.example.moviestreamingapp.request.ApiInterface
import com.example.moviestreamingapp.request.MoviesApi
import com.example.moviestreamingapp.request.SafeApiRequest


class MovieRepo(private val api: MoviesApi) : SafeApiRequest() {

    suspend fun getMovies(str:String,id: Int,key:String) = apiRequest { api.getMovies(str,id,key) }

}