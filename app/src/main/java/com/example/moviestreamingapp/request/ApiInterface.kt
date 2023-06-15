package com.example.moviestreamingapp.request

import com.example.moviestreamingapp.network.movie.*
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("movie/{movie_id}")
    fun getMovies(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String?
    ): Response<List<Movie>>

    // Movie
    @GET("movie/now_playing")
    fun getNowShowingMovies(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int?,
        @Query("region") region: String?
    ): Call<NowShowingMoviesResponse>

    @GET("movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String?,
        @Query("page") page: Int
    ): Call<PopularMoviesResponse>

    @GET("movie/top_rated")
    fun getTopRatedMovies(
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int?,
        @Query("region") region: String?
    ): Call<TopRatedMoviesResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") movieId: Int?,
        @Query("api_key") apiKey: String?
    ): Call<Movie?>?

    @GET("movie/{id}/similar")
    fun getSimilarMovies(
        @Path("id") movieId: Int?,
        @Query("api_key") apiKey: String?,
        @Query("page") page: Int?
    ): Call<SimilarMoviesResponse>

    @GET("discover/movie")
    fun getMoviesByGenre(
        @Query("api_key") apiKey: String?,
        @Query("with_genres") genreNumber: Int?,
        @Query("page") page: Int?
    ): Call<GenreMoviesResponse>
}