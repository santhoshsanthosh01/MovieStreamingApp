package com.example.moviestreamingapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.activities.ViewAllMoviesActivity
import com.example.moviestreamingapp.adapters.MovieBriefSmallAdapter
import com.example.moviestreamingapp.adapters.MovieCarouselAdapter
import com.example.moviestreamingapp.adapters.MoviesNestedRecViewAdapter
import com.example.moviestreamingapp.network.movie.*
import com.example.moviestreamingapp.repositary.MovieRepo
import com.example.moviestreamingapp.request.ApiClient
import com.example.moviestreamingapp.request.ApiInterface
import com.example.moviestreamingapp.request.MoviesApi
import com.example.moviestreamingapp.utils.Constants
import com.example.moviestreamingapp.utils.MovieViewModel
import com.example.moviestreamingapp.utils.NestedRecViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieFragment : Fragment() {
    lateinit var  mProgressBar: ProgressBar
    lateinit var  mViewPopular: TextView
    lateinit var  mViewTopRated: TextView
    private  var  timer: Timer? = null
    private  var  timerTask: TimerTask? = null
     var  position = 0
    lateinit var  carouselLayoutManager: LinearLayoutManager
    lateinit var  mNowShowingRecyclerView: RecyclerView
    lateinit var  mNowShowingMovies: MutableList<MovieBrief>
    lateinit var  mNowShowingAdapter: MovieCarouselAdapter
    lateinit var  mPopularMoviesRecyclerView: RecyclerView
    lateinit var  mPopularMovies: MutableList<MovieBrief>
    lateinit var  mPopularMoviesAdapter: MovieBriefSmallAdapter
    lateinit var  mTopRatedRecyclerView: RecyclerView
    lateinit var  mTopRatedMovies: MutableList<MovieBrief>
    lateinit var  mTopRatedAdapter: MovieBriefSmallAdapter
    lateinit var  mActionMovies: MutableList<MovieBrief>
    lateinit var  mAdventureMovies: MutableList<MovieBrief>
    lateinit var  mAnimatedMovies: MutableList<MovieBrief>
    lateinit var  mComedyMovies: MutableList<MovieBrief>
    lateinit var  mCrimeMovies: MutableList<MovieBrief>
    lateinit var  mDocumentaryMovies: MutableList<MovieBrief>
    lateinit var  mDramaMovies: MutableList<MovieBrief>
    lateinit var  mFamilyMovies: MutableList<MovieBrief>
    lateinit var  mFantasyMovies: MutableList<MovieBrief>
    lateinit var  mHistoryMovies: MutableList<MovieBrief>
    lateinit var  mHorrorMovies: MutableList<MovieBrief>
    lateinit var  mMusicMovies: MutableList<MovieBrief>
    lateinit var  mMysteryMovies: MutableList<MovieBrief>
    lateinit var  mRomanceMovies: MutableList<MovieBrief>
    lateinit var  mSciFiMovies: MutableList<MovieBrief>
    lateinit var  mTvMovies: MutableList<MovieBrief>
    lateinit var  mThrillerMovies: MutableList<MovieBrief>
    lateinit var  mWarMovies: MutableList<MovieBrief>
    lateinit var  mWesternMovies: MutableList<MovieBrief>
    lateinit var  mNestedRecView: RecyclerView
    lateinit var  mNestedList: MutableList<NestedRecViewModel>
    lateinit var  mMoviesNestedRecViewAdapter: MoviesNestedRecViewAdapter
    lateinit var  mPopularHeading: ConstraintLayout
    lateinit var  mTopRatedHeading: ConstraintLayout

    lateinit var viewModel: MovieViewModel


    var  mNowShowingMoviesLoaded = false
     var  mPopularMoviesLoaded = false
     var  mTopRatedMoviesLoaded = false


    lateinit var mNowShowingMoviesCall: Call<NowShowingMoviesResponse>
    lateinit var mPopularMoviesCall: Call<PopularMoviesResponse>
    lateinit var mTopRatedMoviesCall: Call<TopRatedMoviesResponse>
    lateinit var mGenreMoviesResponseCall: Call<GenreMoviesResponse>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mProgressBar = view.findViewById(R.id.movie_progressBar)
        mViewPopular = view.findViewById(R.id.view_popular)
        mViewTopRated = view.findViewById(R.id.view_top_rated)
        mNowShowingRecyclerView = view.findViewById(R.id.carousel_recView)
        mPopularMoviesRecyclerView = view.findViewById(R.id.popular_recView)
        mTopRatedRecyclerView = view.findViewById(R.id.top_rated_recView)
        mPopularHeading = view.findViewById(R.id.popular_heading)
        mTopRatedHeading = view.findViewById(R.id.top_rated_heading)
        mNestedRecView = view.findViewById(R.id.movie_nested_recView)



        mNowShowingMovies = ArrayList<MovieBrief>()
        mPopularMovies = ArrayList<MovieBrief>()
        mTopRatedMovies = ArrayList<MovieBrief>()
        mActionMovies = ArrayList<MovieBrief>()
        mAnimatedMovies = ArrayList<MovieBrief>()
        mAdventureMovies = ArrayList<MovieBrief>()
        mComedyMovies = ArrayList<MovieBrief>()
        mCrimeMovies = ArrayList<MovieBrief>()
        mDocumentaryMovies = ArrayList<MovieBrief>()
        mDramaMovies = ArrayList<MovieBrief>()
        mFamilyMovies = ArrayList<MovieBrief>()
        mFantasyMovies = ArrayList<MovieBrief>()
        mHistoryMovies = ArrayList<MovieBrief>()
        mHorrorMovies = ArrayList<MovieBrief>()
        mMusicMovies = ArrayList<MovieBrief>()
        mMysteryMovies = ArrayList<MovieBrief>()
        mRomanceMovies = ArrayList<MovieBrief>()
        mSciFiMovies = ArrayList<MovieBrief>()
        mTvMovies = ArrayList<MovieBrief>()
        mThrillerMovies = ArrayList<MovieBrief>()
        mWarMovies = ArrayList<MovieBrief>()
        mWesternMovies = ArrayList<MovieBrief>()
        mNestedList = ArrayList<NestedRecViewModel>()


        viewModel = ViewModelProvider(this).get(MovieViewModel::class.java)


        mNowShowingMoviesLoaded = false
        mPopularMoviesLoaded = false
        mTopRatedMoviesLoaded = false

        mNowShowingAdapter =
            context?.let { MovieCarouselAdapter(mNowShowingMovies as ArrayList<MovieBrief>, it) }!!
        carouselLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mNowShowingRecyclerView.setLayoutManager(carouselLayoutManager)
        mNowShowingRecyclerView.setAdapter(mNowShowingAdapter)
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(mNowShowingRecyclerView)
        mNowShowingRecyclerView.smoothScrollBy(5, 0)
        mPopularMoviesAdapter = MovieBriefSmallAdapter(mPopularMovies, requireContext())
        mPopularMoviesRecyclerView.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        mPopularMoviesRecyclerView.setAdapter(mPopularMoviesAdapter)
        mTopRatedAdapter = MovieBriefSmallAdapter(mTopRatedMovies, requireContext())
        mTopRatedRecyclerView.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        mTopRatedRecyclerView.setAdapter(mTopRatedAdapter)
        mMoviesNestedRecViewAdapter = MoviesNestedRecViewAdapter(mNestedList, requireContext())
        mNestedRecView.setLayoutManager(LinearLayoutManager(context))
        mNestedRecView.setAdapter(mMoviesNestedRecViewAdapter)
        mNowShowingRecyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == 1) {
                    stopAutoScrollCarousel()
                } else if (newState == 0) {
                    position = carouselLayoutManager!!.findFirstCompletelyVisibleItemPosition()
                    runAutoScrollingCarousel()
                }
            }
        })
        mViewPopular.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ViewAllMoviesActivity::class.java)
            intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.POPULAR_MOVIES_TYPE)
            startActivity(intent)
        })
        mViewTopRated.setOnClickListener(View.OnClickListener {
            val intent = Intent(context, ViewAllMoviesActivity::class.java)
            intent.putExtra(Constants.VIEW_ALL_MOVIES_TYPE, Constants.TOP_RATED_MOVIES_TYPE)
            startActivity(intent)
        })
        initViews()
    }

    private fun stopAutoScrollCarousel() {
        if (timer != null && timerTask != null) {
            timerTask!!.cancel()
            timer!!.cancel()
            timer = null!!
            timerTask = null!!
            position = carouselLayoutManager.findFirstCompletelyVisibleItemPosition()
        }
    }

    private fun runAutoScrollingCarousel() {
        if (false) {
            timer = Timer()
            timerTask = object : TimerTask() {
                override fun run() {
                    if (position == mNowShowingMovies!!.size - 1) {
                        mNowShowingRecyclerView!!.post {
                            position = 0
                            mNowShowingRecyclerView!!.smoothScrollToPosition(position)
                            mNowShowingRecyclerView!!.smoothScrollBy(5, 0)
                        }
                    } else {
                        position++
                        mNowShowingRecyclerView!!.smoothScrollToPosition(position)
                    }
                }
            }
            timer!!.schedule(timerTask, 4000, 4000)
        }
    }

    private fun initViews() {

        viewModel.loadNowShowingMovies(mNowShowingMovies,mNestedList,mNowShowingAdapter)
        viewModel.loadPopularMovies(mPopularMovies,mNestedList,mPopularMoviesAdapter)
        viewModel.loadTopRatedMovies(mTopRatedMovies,mNestedList,mTopRatedAdapter)
        viewModel.loadActionMovies(mActionMovies,mNestedList,mNestedRecView)
        viewModel.loadAdventureMovies(mAdventureMovies,mNestedList,mNestedRecView)
        viewModel.loadAnimatedMovies(mAnimatedMovies,mNestedList)
        viewModel.loadComedyMovies(mComedyMovies,mNestedList)
        viewModel.loadCrimeMovies(mCrimeMovies,mNestedList)
        viewModel.loadDocumentaryMovies(mDocumentaryMovies,mNestedList,mMoviesNestedRecViewAdapter)
        viewModel.loadDramaMovies(mDramaMovies,mNestedList)
        viewModel.loadFamilyMovies(mFamilyMovies,mNestedList)
        viewModel.loadFantasyMovies(mFantasyMovies,mNestedList)
        viewModel.loadHistoryMovies(mHistoryMovies,mNestedList)
        viewModel.loadHorrorMovies(mHorrorMovies,mNestedList)
        viewModel.loadMusicMovies(mMusicMovies,mNestedList)
        viewModel.loadMysteryMovies(mMysteryMovies,mNestedList)
        viewModel.loadRomanceMovies(mRomanceMovies,mNestedList)
        viewModel.loadSciFiMovies(mSciFiMovies,mNestedList)
        viewModel.loadTvMovies(mTvMovies,mNestedList)
        viewModel.loadWarMovies(mWarMovies,mNestedList)
        viewModel.loadThriller(mThrillerMovies,mNestedList)
        viewModel.loadWesternMovies(mWesternMovies,mNestedList,mMoviesNestedRecViewAdapter)
        checkAllDataLoaded()

    }



    private fun checkAllDataLoaded() {

            mProgressBar!!.visibility = View.GONE
            mNowShowingRecyclerView!!.visibility = View.VISIBLE
            mPopularHeading!!.visibility = View.VISIBLE
            mPopularMoviesRecyclerView!!.visibility = View.VISIBLE
            mTopRatedHeading!!.visibility = View.VISIBLE
            mTopRatedRecyclerView!!.visibility = View.VISIBLE

    }

    override fun onPause() {
        super.onPause()
        stopAutoScrollCarousel()
    }

    override fun onResume() {
        super.onResume()
        runAutoScrollingCarousel()
    }
}