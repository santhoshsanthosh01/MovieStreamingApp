package com.example.moviestreamingapp.activities


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.adapters.MovieBriefSmallAdapter
import com.example.moviestreamingapp.network.movie.GenreMoviesResponse
import com.example.moviestreamingapp.network.movie.MovieBrief
import com.example.moviestreamingapp.network.movie.PopularMoviesResponse
import com.example.moviestreamingapp.network.movie.TopRatedMoviesResponse
import com.example.moviestreamingapp.request.ApiClient
import com.example.moviestreamingapp.request.ApiInterface
import com.example.moviestreamingapp.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewAllMoviesActivity : AppCompatActivity() {
    private var mRecyclerView: RecyclerView? = null
    private var mMovies: MutableList<MovieBrief>? = null
    private var mMoviesAdapter: MovieBriefSmallAdapter? = null
    private var mMovieType = 0
    private var pagesOver = false
    private var presentPage = 1
    private var loading = true
    private var previousTotal = 0
    private val visibleThreshold = 5
    lateinit var mPopularMoviesCall: Call<PopularMoviesResponse>
    lateinit var mTopRatedMoviesCall: Call<TopRatedMoviesResponse>
    lateinit var mGenreMoviesResponseCall: Call<GenreMoviesResponse>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_movies)
        val toolbar = findViewById<View>(R.id.view_movies_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val receivedIntent = intent
        mMovieType = receivedIntent.getIntExtra(Constants.VIEW_ALL_MOVIES_TYPE, -1)
        if (mMovieType == -1) finish()
        when (mMovieType) {
            Constants.POPULAR_MOVIES_TYPE -> title = "Popular Movies"
            Constants.TOP_RATED_MOVIES_TYPE -> title = "Top Rated Movies"
            Constants.ACTION_MOVIES_TYPE -> title = "Action Movies"
            Constants.ADVENTURE_MOVIES_TYPE -> title = "Adventure Movies"
            Constants.ANIMATION_MOVIES_TYPE -> title = "Animation Movies"
            Constants.COMEDY_MOVIES_TYPE -> title = "Comedy Movies"
            Constants.CRIME_MOVIES_TYPE -> title = "Crime Movies"
            Constants.DOCUMENTARY_MOVIES_TYPE -> title = "Documentary Movies"
            Constants.DRAMA_MOVIES_TYPE -> title = "Drama Movies"
            Constants.FAMILY_MOVIES_TYPE -> title = "Family Movies"
            Constants.FANTASY_MOVIES_TYPE -> title = "Fantasy Movies"
            Constants.HISTORY_MOVIES_TYPE -> title = "History Movies"
            Constants.HORROR_MOVIES_TYPE -> title = "Horror Movies"
            Constants.MUSIC_MOVIES_TYPE -> title = "Music Movies"
            Constants.MYSTERY_MOVIES_TYPE -> title = "Mystery Movies"
            Constants.ROMANCE_MOVIES_TYPE -> title = "Romance Movies"
            Constants.SCIFI_MOVIES_TYPE -> title = "Sci-Fi Movies"
            Constants.TV_MOVIES_TYPE -> title = "TV Movies"
            Constants.THRILLER_MOVIES_TYPE -> title = "Thriller Movies"
            Constants.WAR_MOVIES_TYPE -> title = "War Movies"
            Constants.WESTERN_MOVIES_TYPE -> title = "Western Movies"
        }
        mRecyclerView = findViewById<View>(R.id.view_movies_recView) as RecyclerView
        mMovies = ArrayList<MovieBrief>()
        mMoviesAdapter = MovieBriefSmallAdapter(mMovies as ArrayList<MovieBrief>, this@ViewAllMoviesActivity)
        mRecyclerView!!.adapter = mMoviesAdapter
        val gridLayoutManager = GridLayoutManager(this@ViewAllMoviesActivity, 3)
        mRecyclerView!!.layoutManager = gridLayoutManager
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = gridLayoutManager.childCount
                val totalItemCount = gridLayoutManager.itemCount
                val firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition()
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false
                        previousTotal = totalItemCount
                    }
                }
                if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem + visibleThreshold) {
                    loadMovies(mMovieType)
                    loading = true
                }
            }
        })
        loadMovies(mMovieType)
    }

    private fun loadMovies(movieType: Int) {
        if (pagesOver) return
        val apiService: ApiInterface = ApiClient.movieApi
        when (movieType) {
            Constants.POPULAR_MOVIES_TYPE -> {
                mPopularMoviesCall = apiService.getPopularMovies(Constants.API_KEY, presentPage)
                mPopularMoviesCall!!.enqueue(object : Callback<PopularMoviesResponse> {
                    override fun onResponse(
                        call: Call<PopularMoviesResponse>,
                        response: Response<PopularMoviesResponse>
                    ) {
                        if (!response.isSuccessful()) {
                            mPopularMoviesCall = call.clone()
                            mPopularMoviesCall!!.enqueue(this)
                            return
                        }
                        if (response.body() == null) return
                        if (response.body()!!.results == null) return
                        for (movieBrief in response.body()!!.results) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null) mMovies!!.add(
                                movieBrief
                            )
                        }
                        mMoviesAdapter!!.notifyDataSetChanged()
                        if (response.body()!!.getPage() === response.body()!!
                                .getTotalPages()
                        ) pagesOver = true else presentPage++
                    }

                    override fun onFailure(call: Call<PopularMoviesResponse?>, t: Throwable) {}
                })
            }
            Constants.TOP_RATED_MOVIES_TYPE -> {
                mTopRatedMoviesCall =
                    apiService.getTopRatedMovies(Constants.API_KEY, presentPage, "US")
                mTopRatedMoviesCall!!.enqueue(object : Callback<TopRatedMoviesResponse> {
                    override fun onResponse(
                        call: Call<TopRatedMoviesResponse>,
                        response: Response<TopRatedMoviesResponse>
                    ) {
                        if (!response.isSuccessful()) {
                            mTopRatedMoviesCall = call.clone()
                            mTopRatedMoviesCall!!.enqueue(this)
                            return
                        }
                        if (response.body() == null) return
                        if (response.body()!!.getResults() == null) return
                        for (movieBrief in response.body()!!.getResults()) {
                            if (movieBrief != null && movieBrief.getTitle() != null && movieBrief.getPosterPath() != null) mMovies!!.add(
                                movieBrief
                            )
                        }
                        mMoviesAdapter!!.notifyDataSetChanged()
                        if (response.body()!!.getPage() === response.body()!!
                                .getTotalPages()
                        ) pagesOver = true else presentPage++
                    }

                    override fun onFailure(call: Call<TopRatedMoviesResponse?>, t: Throwable) {}
                })
            }
            else -> {
                mGenreMoviesResponseCall = apiService.getMoviesByGenre(Constants.API_KEY, movieType, presentPage)
                mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                    override fun onResponse(
                        call: Call<GenreMoviesResponse>,
                        response: Response<GenreMoviesResponse>
                    ) {
                        if (!response.isSuccessful()) {
                            mGenreMoviesResponseCall = call.clone()
                            mGenreMoviesResponseCall!!.enqueue(this)
                            return
                        }
                        if (response.body() == null) return
                        if (response.body()!!.results == null) return
                        for (movieBrief in response.body()!!.results) {
                            if (movieBrief?.posterPath != null) {
                                mMovies!!.add(movieBrief)
                            }
                        }
                        mMoviesAdapter!!.notifyDataSetChanged()
                        if (response.body()!!.getPage()
                                .equals(response.body()!!.getTotalPages())
                        ) pagesOver = true else presentPage++
                    }

                    override fun onFailure(call: Call<GenreMoviesResponse?>, t: Throwable) {}
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}