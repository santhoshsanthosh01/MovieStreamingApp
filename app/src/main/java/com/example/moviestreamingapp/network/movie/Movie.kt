package com.example.moviestreamingapp.network.movie

import com.google.gson.annotations.SerializedName

class Movie {
    @SerializedName("genres")
    var genres: List<Genre>? = null

    @SerializedName("adult")
     val adult: Boolean? = null

    @SerializedName("backdrop_path")
     val backdropPath: String? = null

    //belongs_to_collection missing
    @SerializedName("budget")
     val budget: Int? = null

    @SerializedName("homepage")
     val homepage: String? = null

    @SerializedName("id")
     val id: Int? = null

    @SerializedName("imdb_id")
     val imdbId: String? = null

    @SerializedName("original_language")
     val originalLanguage: String? = null

    @SerializedName("original_title")
     val originalTitle: String? = null

    @SerializedName("overview")
     val overview: String? = null

    @SerializedName("popularity")
     val popularity: Double? = null

    @SerializedName("poster_path")
     val posterPath: String? = null

    //production_companies missing
    //production_countries missing
    @SerializedName("release_date")
     val releaseDate: String? = null

    @SerializedName("revenue")
     val revenue: Int? = null

    @SerializedName("runtime")
     val runtime: Int? = null

    //spoken_languages missing
    @SerializedName("status")
     val status: String? = null

    @SerializedName("tagline")
     val tagline: String? = null

    @SerializedName("title")
    var title: String? = null

    @SerializedName("video")
     val video: Boolean? = null

    @SerializedName("vote_average")
     val voteAverage: Double? = null

    @SerializedName("vote_count")
     val voteCount: Int? = null
}