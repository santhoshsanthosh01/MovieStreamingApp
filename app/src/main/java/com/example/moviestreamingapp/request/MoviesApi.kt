package com.example.moviestreamingapp.request

import com.example.moviestreamingapp.network.movie.Movie
import com.example.moviestreamingapp.network.movie.NowShowingMoviesResponse
import com.example.moviestreamingapp.utils.Constants
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface MoviesApi {

    @GET("movie/now_playing")
    suspend fun getMovies(str:String,id:Int,key:String) : Response<List<NowShowingMoviesResponse>>

    companion object{
        operator fun invoke(): MoviesApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(Constants.BASE_URL)
                .build()
                .create(MoviesApi::class.java)
        }
    }

}