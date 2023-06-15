package com.example.moviestreamingapp.utils


import com.example.moviestreamingapp.network.movie.MovieBrief

class NestedRecViewModel(mMovies: List<MovieBrief>, mGenreId: Int) {
    private var mMovies: List<MovieBrief>
    private var mGenreId: Int

    init {
        this.mMovies = mMovies
        this.mGenreId = mGenreId
    }

    fun getmMovies(): List<MovieBrief> {
        return mMovies
    }

    fun setmMovies(mMovies: List<MovieBrief>) {
        this.mMovies = mMovies
    }

    fun getmGenreId(): Int {
        return mGenreId
    }

    fun setmGenreId(mGenreId: Int) {
        this.mGenreId = mGenreId
    }
}