package com.example.moviestreamingapp.utils


import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView

import androidx.work.*
import com.example.moviestreamingapp.adapters.MovieBriefSmallAdapter
import com.example.moviestreamingapp.adapters.MovieCarouselAdapter
import com.example.moviestreamingapp.adapters.MoviesNestedRecViewAdapter
import com.example.moviestreamingapp.fragments.MovieFragment
import com.example.moviestreamingapp.network.movie.*
import com.example.moviestreamingapp.request.ApiClient
import com.example.moviestreamingapp.request.ApiInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieViewModel : ViewModel() {
    private val gConstraints =
        Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    private var gWorkmanager: WorkManager = WorkManager.getInstance()
    private var gArrayList: ArrayList<Movie> = ArrayList()

    //For Updating Individual item in recycler view

    var gItemPosition: MutableLiveData<Int> = MutableLiveData(0)


     fun loadWesternMovies(mWesternMovies : MutableList<MovieBrief>,
                           mNestedList : MutableList<NestedRecViewModel>,
                           mMoviesNestedRecViewAdapter : MoviesNestedRecViewAdapter
     ) {

         viewModelScope.launch(Dispatchers.IO) {

             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.WESTERN_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mWesternMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mWesternMovies!!,
                             Constants.WESTERN_MOVIES_TYPE
                         )
                     )
                     mMoviesNestedRecViewAdapter.notifyDataSetChanged()
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }


    fun loadWarMovies(mWarMovies : MutableList<MovieBrief>,
                              mNestedList : MutableList<NestedRecViewModel>) {
        viewModelScope.launch(Dispatchers.IO) {


            val apiInterface: ApiInterface = ApiClient.movieApi
            var mGenreMoviesResponseCall =
                apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.WAR_MOVIES_TYPE, 1)
            mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                override fun onResponse(
                    call: Call<GenreMoviesResponse>,
                    response: Response<GenreMoviesResponse>
                ) {
                    if (!response.isSuccessful()) {
                        mGenreMoviesResponseCall = call.clone()
                        mGenreMoviesResponseCall.enqueue(this)
                        return
                    }
                    if (response.body() == null) return
                    if (response.body()!!.getResults() == null) return
                    for (movieBrief in response.body()!!.getResults()) {
                        if (movieBrief != null && movieBrief.getPosterPath() != null) {
                            mWarMovies!!.add(movieBrief)
                        }
                    }
                    mNestedList!!.add(NestedRecViewModel(mWarMovies!!, Constants.WAR_MOVIES_TYPE))
                }

                override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
            })
        }
    }

     fun loadThriller(mThrillerMovies : MutableList<MovieBrief>,
                      mNestedList : MutableList<NestedRecViewModel>) {

         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.THRILLER_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mThrillerMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mThrillerMovies!!,
                             Constants.THRILLER_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

    fun loadTvMovies(mTvMovies : MutableList<MovieBrief>,
                     mNestedList : MutableList<NestedRecViewModel>) {

        viewModelScope.launch(Dispatchers.IO) {


            val apiInterface: ApiInterface = ApiClient.movieApi
            var mGenreMoviesResponseCall =
                apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.TV_MOVIES_TYPE, 1)
            mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                override fun onResponse(
                    call: Call<GenreMoviesResponse>,
                    response: Response<GenreMoviesResponse>
                ) {
                    if (!response.isSuccessful()) {
                        mGenreMoviesResponseCall = call.clone()
                        mGenreMoviesResponseCall.enqueue(this)
                        return
                    }
                    if (response.body() == null) return
                    if (response.body()!!.getResults() == null) return
                    for (movieBrief in response.body()!!.getResults()) {
                        if (movieBrief != null && movieBrief.getPosterPath() != null) {
                            mTvMovies!!.add(movieBrief)
                        }
                    }
                    mNestedList!!.add(NestedRecViewModel(mTvMovies!!, Constants.TV_MOVIES_TYPE))
                }

                override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
            })
        }
    }

    fun loadSciFiMovies(mSciFiMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {

        viewModelScope.launch(Dispatchers.IO) {


            val apiInterface: ApiInterface = ApiClient.movieApi
            var mGenreMoviesResponseCall =
                apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.SCIFI_MOVIES_TYPE, 1)
            mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                override fun onResponse(
                    call: Call<GenreMoviesResponse>,
                    response: Response<GenreMoviesResponse>
                ) {
                    if (!response.isSuccessful()) {
                        mGenreMoviesResponseCall = call.clone()
                        mGenreMoviesResponseCall.enqueue(this)
                        return
                    }
                    if (response.body() == null) return
                    if (response.body()!!.getResults() == null) return
                    for (movieBrief in response.body()!!.getResults()) {
                        if (movieBrief != null && movieBrief.getPosterPath() != null) {
                            mSciFiMovies!!.add(movieBrief)
                        }
                    }
                    mNestedList!!.add(
                        NestedRecViewModel(
                            mSciFiMovies!!,
                            Constants.SCIFI_MOVIES_TYPE
                        )
                    )
                }

                override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
            })
        }
    }

    fun loadRomanceMovies(mRomanceMovies : MutableList<MovieBrief>,
                          mNestedList : MutableList<NestedRecViewModel>) {

        viewModelScope.launch(Dispatchers.IO) {


            val apiInterface: ApiInterface = ApiClient.movieApi
            var mGenreMoviesResponseCall =
                apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.ROMANCE_MOVIES_TYPE, 1)
            mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                override fun onResponse(
                    call: Call<GenreMoviesResponse>,
                    response: Response<GenreMoviesResponse>
                ) {
                    if (!response.isSuccessful()) {
                        mGenreMoviesResponseCall = call.clone()
                        mGenreMoviesResponseCall.enqueue(this)
                        return
                    }
                    if (response.body() == null) return
                    if (response.body()!!.getResults() == null) return
                    for (movieBrief in response.body()!!.getResults()) {
                        if (movieBrief != null && movieBrief.getPosterPath() != null) {
                            mRomanceMovies!!.add(movieBrief)
                        }
                    }
                    mNestedList!!.add(
                        NestedRecViewModel(
                            mRomanceMovies!!,
                            Constants.ROMANCE_MOVIES_TYPE
                        )
                    )
                }

                override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
            })
        }
    }

    fun loadMysteryMovies(mMysteryMovies : MutableList<MovieBrief>,
                          mNestedList : MutableList<NestedRecViewModel>) {

        viewModelScope.launch(Dispatchers.IO) {


            val apiInterface: ApiInterface = ApiClient.movieApi
            var mGenreMoviesResponseCall =
                apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.MYSTERY_MOVIES_TYPE, 1)
            mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                override fun onResponse(
                    call: Call<GenreMoviesResponse>,
                    response: Response<GenreMoviesResponse>
                ) {
                    if (!response.isSuccessful()) {
                        mGenreMoviesResponseCall = call.clone()
                        mGenreMoviesResponseCall.enqueue(this)
                        return
                    }
                    if (response.body() == null) return
                    if (response.body()!!.getResults() == null) return
                    for (movieBrief in response.body()!!.getResults()) {
                        if (movieBrief != null && movieBrief.getPosterPath() != null) {
                            mMysteryMovies!!.add(movieBrief)
                        }
                    }
                    mNestedList!!.add(
                        NestedRecViewModel(
                            mMysteryMovies!!,
                            Constants.MYSTERY_MOVIES_TYPE
                        )
                    )
                }

                override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
            })
        }
    }

    fun loadMusicMovies(mMusicMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
        viewModelScope.launch(Dispatchers.IO) {


            val apiInterface: ApiInterface = ApiClient.movieApi
            var mGenreMoviesResponseCall =
                apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.MUSIC_MOVIES_TYPE, 1)
            mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                override fun onResponse(
                    call: Call<GenreMoviesResponse>,
                    response: Response<GenreMoviesResponse>
                ) {
                    if (!response.isSuccessful()) {
                        mGenreMoviesResponseCall = call.clone()
                        mGenreMoviesResponseCall.enqueue(this)
                        return
                    }
                    if (response.body() == null) return
                    if (response.body()!!.getResults() == null) return
                    for (movieBrief in response.body()!!.getResults()) {
                        if (movieBrief != null && movieBrief.getPosterPath() != null) {
                            mMusicMovies!!.add(movieBrief)
                        }
                    }
                    mNestedList!!.add(
                        NestedRecViewModel(
                            mMusicMovies!!,
                            Constants.MUSIC_MOVIES_TYPE
                        )
                    )
                }

                override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
            })
        }
    }


     fun loadNowShowingMovies(mNowShowingMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>,
                              mNowShowingAdapter: MovieCarouselAdapter
     ) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi

             var mNowShowingMoviesCall =
                 apiInterface.getNowShowingMovies(Constants.API_KEY, 1, "US")
             (mNowShowingMoviesCall as Call<NowShowingMoviesResponse>?)!!.enqueue(object :
                 Callback<NowShowingMoviesResponse> {
                 override fun onResponse(
                     call: Call<NowShowingMoviesResponse>,
                     response: Response<NowShowingMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mNowShowingMoviesCall = call.clone()
                         mNowShowingMoviesCall!!.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.results) {
                         if (movieBrief?.backdropPath != null) mNowShowingMovies!!.add(
                             movieBrief
                         )
                     }
                     mNowShowingAdapter!!.notifyDataSetChanged()

                 }

                 override fun onFailure(call: Call<NowShowingMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadPopularMovies(mPopularMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>,
                           mPopularMoviesAdapter: MovieBriefSmallAdapter) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mPopularMoviesCall = apiInterface.getPopularMovies(Constants.API_KEY, 1)
             (mPopularMoviesCall as Call<PopularMoviesResponse>?)!!.enqueue(object :
                 Callback<PopularMoviesResponse> {
                 override fun onResponse(
                     call: Call<PopularMoviesResponse>,
                     response: Response<PopularMoviesResponse>
                 ) {
                     if (!response.isSuccessful) {
                         mPopularMoviesCall = call.clone()
                         mPopularMoviesCall!!.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief?.posterPath != null) mPopularMovies!!.add(
                             movieBrief
                         )
                     }
                     mPopularMoviesAdapter!!.notifyDataSetChanged()

                 }

                 override fun onFailure(call: Call<PopularMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadTopRatedMovies(mTopRatedMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>,
                            mTopRatedAdapter: MovieBriefSmallAdapter
     ) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mTopRatedMoviesCall = apiInterface.getTopRatedMovies(Constants.API_KEY, 1, "US")
             (mTopRatedMoviesCall as Call<TopRatedMoviesResponse>?)!!.enqueue(object :
                 Callback<TopRatedMoviesResponse> {
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
                     for (movieBrief in response.body()!!.results) {
                         if (movieBrief?.posterPath != null) mTopRatedMovies!!.add(
                             movieBrief
                         )
                     }
                     mTopRatedAdapter!!.notifyDataSetChanged()


                 }

                 override fun onFailure(call: Call<TopRatedMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadActionMovies(mActionMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>,
                          mNestedRecView: RecyclerView) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.ACTION_MOVIES_TYPE, 1)
             (mGenreMoviesResponseCall as Call<GenreMoviesResponse>?)!!.enqueue(object :
                 Callback<GenreMoviesResponse> {
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
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief?.posterPath != null) {
                             mActionMovies!!.add(movieBrief)
                         }
                     }
                     mActionMovies?.let { NestedRecViewModel(it, Constants.ACTION_MOVIES_TYPE) }
                         ?.let { mNestedList!!.add(it) }
                     mNestedRecView!!.visibility = View.VISIBLE
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadAdventureMovies(mAdventureMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>,
                             mNestedRecView: RecyclerView) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(
                     Constants.API_KEY,
                     Constants.ADVENTURE_MOVIES_TYPE,
                     1
                 )
             (mGenreMoviesResponseCall as Call<GenreMoviesResponse>?)!!.enqueue(object :
                 Callback<GenreMoviesResponse> {
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
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mAdventureMovies!!.add(movieBrief)
                         }
                     }
                     mAdventureMovies?.let {
                         NestedRecViewModel(
                             it,
                             Constants.ADVENTURE_MOVIES_TYPE
                         )
                     }?.let {
                         mNestedList!!.add(
                             it
                         )
                     }
                     mNestedRecView!!.visibility = View.VISIBLE
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadAnimatedMovies(mAnimatedMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(
                     Constants.API_KEY,
                     Constants.ANIMATION_MOVIES_TYPE,
                     1
                 )
             (mGenreMoviesResponseCall as Call<GenreMoviesResponse>?)!!.enqueue(object :
                 Callback<GenreMoviesResponse> {
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
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief?.posterPath != null) {
                             mAnimatedMovies!!.add(movieBrief)
                         }
                     }
                     mAnimatedMovies?.let {
                         NestedRecViewModel(
                             it,
                             Constants.ANIMATION_MOVIES_TYPE
                         )
                     }?.let {
                         mNestedList!!.add(
                             it
                         )
                     }
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadComedyMovies(mComedyMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.COMEDY_MOVIES_TYPE, 1)
             (mGenreMoviesResponseCall as Call<GenreMoviesResponse>?)!!.enqueue(object :
                 Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mComedyMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mComedyMovies!!,
                             Constants.COMEDY_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadCrimeMovies(mCrimeMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.CRIME_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mCrimeMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mCrimeMovies!!,
                             Constants.CRIME_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadDocumentaryMovies(mDocumentaryMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>,
                               mMoviesNestedRecViewAdapter : MoviesNestedRecViewAdapter) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(
                     Constants.API_KEY,
                     Constants.DOCUMENTARY_MOVIES_TYPE,
                     1
                 )
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mDocumentaryMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mDocumentaryMovies!!,
                             Constants.DOCUMENTARY_MOVIES_TYPE
                         )
                     )
                     mMoviesNestedRecViewAdapter.notifyDataSetChanged()
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadDramaMovies(mDramaMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.DRAMA_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mDramaMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mDramaMovies!!,
                             Constants.DRAMA_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadFamilyMovies(mFamilyMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.FAMILY_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mFamilyMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mFamilyMovies!!,
                             Constants.FAMILY_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadFantasyMovies(mFantasyMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.FANTASY_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mFantasyMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mFantasyMovies!!,
                             Constants.FANTASY_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadHistoryMovies(mHistoryMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.HISTORY_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mHistoryMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mHistoryMovies!!,
                             Constants.HISTORY_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }

     fun loadHorrorMovies(mHorrorMovies : MutableList<MovieBrief>,
                        mNestedList : MutableList<NestedRecViewModel>) {
         viewModelScope.launch(Dispatchers.IO) {


             val apiInterface: ApiInterface = ApiClient.movieApi
             var mGenreMoviesResponseCall =
                 apiInterface.getMoviesByGenre(Constants.API_KEY, Constants.HORROR_MOVIES_TYPE, 1)
             mGenreMoviesResponseCall.enqueue(object : Callback<GenreMoviesResponse> {
                 override fun onResponse(
                     call: Call<GenreMoviesResponse>,
                     response: Response<GenreMoviesResponse>
                 ) {
                     if (!response.isSuccessful()) {
                         mGenreMoviesResponseCall = call.clone()
                         mGenreMoviesResponseCall.enqueue(this)
                         return
                     }
                     if (response.body() == null) return
                     if (response.body()!!.getResults() == null) return
                     for (movieBrief in response.body()!!.getResults()) {
                         if (movieBrief != null && movieBrief.getPosterPath() != null) {
                             mHorrorMovies!!.add(movieBrief)
                         }
                     }
                     mNestedList!!.add(
                         NestedRecViewModel(
                             mHorrorMovies!!,
                             Constants.HORROR_MOVIES_TYPE
                         )
                     )
                 }

                 override fun onFailure(call: Call<GenreMoviesResponse?>?, t: Throwable?) {}
             })
         }
    }




}