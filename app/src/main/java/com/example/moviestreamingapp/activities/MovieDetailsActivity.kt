package com.example.moviestreamingapp.activities


import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.database.movies.FavMovie
import com.example.moviestreamingapp.databinding.ActivityMovieDetailsBinding
import com.example.moviestreamingapp.network.movie.Movie
import com.example.moviestreamingapp.repositary.MovieRepo
import com.example.moviestreamingapp.request.ApiClient
import com.example.moviestreamingapp.request.ApiInterface
import com.example.moviestreamingapp.request.MoviesApi
import com.example.moviestreamingapp.utils.Constants
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MovieDetailsActivity : AppCompatActivity() {
    private var movieId = 0
    private var imdbId = "tt0137523"
    lateinit var bindig: ActivityMovieDetailsBinding
    private val TAG = "PlayerActivity"
    private var movie_back_btn: ImageView? = null
    private var movie_title: TextView? = null
    private var movie_year: TextView? = null
    private var movie_genre: TextView? = null
    private var movie_duration: TextView? = null
    private var movie_story_line: TextView? = null
    private var movie_year_separator: TextView? = null
    private var movie_genre_separator: TextView? = null
    private var movie_story_line_heading: TextView? = null
    private var movie_recommended: RecyclerView? = null
    private var mMovieDetailsCall: Call<Movie?>? = null


    private val STATE_RESUME_WINDOW = "resumeWindow"
    private val STATE_RESUME_POSITION = "resumePosition"
    private val STATE_PLAYER_FULLSCREEN = "playerFullscreen"

    var mExoPlayerFullscreen = false

    lateinit var dataSourceFactory: DataSource.Factory

    var exoPlayer: ExoPlayer? = null
    var bt_fullscreen: ImageView? = null
    var isFullScreen = false
    var isLock = false
    var handler: Handler? = null

    private var mResumeWindow = 0
    private var mResumePosition: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindig = DataBindingUtil.setContentView(this, R.layout.activity_movie_details)


        dataSourceFactory = DefaultDataSourceFactory(
            this, Util.getUserAgent(this, getString(R.string.app_name))
        )

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW)
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION)
            mExoPlayerFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN)
        }


        handler = Handler(Looper.getMainLooper())

        val playerView = findViewById<PlayerView>(R.id.player)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        bt_fullscreen = findViewById<ImageView>(R.id.bt_fullscreen)
        val bt_lockscreen = findViewById<ImageView>(R.id.exo_lock)



        bt_fullscreen!!.setOnClickListener { view ->
            requestedOrientation = if (!isFullScreen) {
                bt_fullscreen!!.setImageDrawable(
                    ContextCompat
                        .getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen_exit)
                )

                movie_title!!.visibility = View.GONE
                movie_story_line_heading!!.visibility = View.GONE

                findViewById<RecyclerView>(R.id.movie_details_recommended).visibility = View.GONE
                findViewById<TextView>(R.id.movie_details_recommended_heading).visibility =
                    View.GONE
                findViewById<RecyclerView>(R.id.movie_details_cast).visibility = View.GONE
                findViewById<TextView>(R.id.movie_details_cast_heading).visibility = View.GONE
                findViewById<TextView>(R.id.movie_details_storyline_heading).visibility = View.GONE
                findViewById<LinearLayout>(R.id.movie_details_linear_group).visibility = View.GONE
                findViewById<TextView>(R.id.movie_details_title).visibility = View.GONE
                findViewById<TextView>(R.id.movie_details_storyline).visibility = View.GONE

                ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                bt_fullscreen!!.setImageDrawable(
                    ContextCompat
                        .getDrawable(applicationContext, R.drawable.ic_baseline_fullscreen)
                )

                movie_title!!.visibility = View.VISIBLE
                movie_story_line_heading!!.visibility = View.VISIBLE

                findViewById<LinearLayout>(R.id.movie_details_linear_group).visibility = View.GONE
                findViewById<TextView>(R.id.movie_details_recommended_heading).visibility =
                    View.GONE
                findViewById<TextView>(R.id.movie_details_storyline_heading).visibility =
                    View.VISIBLE
                findViewById<TextView>(R.id.movie_details_title).visibility = View.VISIBLE
                findViewById<TextView>(R.id.movie_details_storyline).visibility = View.VISIBLE

                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isFullScreen = !isFullScreen
        }
        bt_lockscreen.setOnClickListener { view ->

            if (!isLock) {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_lock
                    )
                )
            } else {
                bt_lockscreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_outline_lock_open
                    )
                )
            }
            isLock = !isLock

            lockScreen(isLock)
        }

        //10000 millisecond = 10 second
        exoPlayer = ExoPlayer.Builder(this)
            .setSeekBackIncrementMs(10000)
            .setSeekForwardIncrementMs(10000)
            .build()
        playerView.player = exoPlayer

        playerView.keepScreenOn = true

        exoPlayer!!.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {

                if (playbackState == Player.STATE_BUFFERING) {
                    progressBar.setVisibility(View.VISIBLE)
                } else if (playbackState == Player.STATE_READY) {

                    progressBar.setVisibility(View.GONE)
                }
                if (!exoPlayer!!.playWhenReady) {
                    handler!!.removeCallbacks(updateProgressAction)
                } else {
                    onProgress()
                }
            }
        })

        val videoUrl = Uri.parse("https://www.rmp-streaming.com/media/big-buck-bunny-360p.mp4")
        val media = MediaItem.fromUri(videoUrl)
        exoPlayer!!.setMediaItem(media)
        exoPlayer!!.prepare()
        exoPlayer!!.play()

        movie_back_btn = findViewById(R.id.movie_details_back_btn)

        movie_story_line_heading = findViewById(R.id.movie_details_storyline_heading)

        movie_title = findViewById(R.id.movie_details_title)
        movie_year = findViewById(R.id.movie_details_year)
        movie_genre = findViewById(R.id.movie_details_genre)
        movie_duration = findViewById(R.id.movie_details_duration)
        movie_story_line = findViewById(R.id.movie_details_storyline)
        movie_recommended = findViewById(R.id.movie_details_recommended)
        movie_year_separator = findViewById(R.id.movie_details_year_separator)
        movie_genre_separator = findViewById(R.id.movie_details_genre_separator)



        val receivedIntent = intent
        movieId = receivedIntent.getIntExtra("movie_id", -1)





        if (movieId == -1) finish()
        val favMovie: FavMovie? = intent.getSerializableExtra("name") as FavMovie?

        movie_back_btn!!.setOnClickListener(View.OnClickListener { onBackPressed() })
        loadActivity()

       /* bindig.movie = mMovie.also { it ->
            mMovie.title = it.title
        }*/




    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow)
        outState.putLong(STATE_RESUME_POSITION, mResumePosition)
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, mExoPlayerFullscreen)
        super.onSaveInstanceState(outState)
    }

    private fun loadActivity() {
        val apiInterface: ApiInterface = ApiClient.movieApi

        mMovieDetailsCall = apiInterface.getMovieDetails(movieId, Constants.API_KEY)
        mMovieDetailsCall!!.enqueue(object : Callback<Movie?> {
            override fun onResponse(call: Call<Movie?>, response: Response<Movie?>) {
                if (!response.isSuccessful()) {
                    mMovieDetailsCall = call.clone()
                    mMovieDetailsCall!!.enqueue(this)
                    return
                }
                if (response.body() == null) return
                imdbId = response.body()!!.imdbId.toString()



                Log.d("chick",""+response.body()!!.title)

                if (response.body()!!.title != null) movie_title!!.setText(
                    response.body()!!.title
                ) else movie_title!!.text =
                    ""
                if (response.body()!!.overview != null && !response.body()!!.overview.toString()
                        .trim()
                        .isEmpty()
                ) {
                    movie_story_line_heading!!.visibility = View.VISIBLE
                    movie_story_line!!.setText(response.body()!!.overview.toString())
                } else {
                    movie_story_line!!.text = ""
                }

            }

            override fun onFailure(call: Call<Movie?>, t: Throwable) {}
        })
    }


    private val updateProgressAction = Runnable { onProgress() }

    //at 4 second
    var ad: Long = 4000
    var check = false
    private fun onProgress() {
        val player = exoPlayer
        val position = player?.currentPosition ?: 0
        handler!!.removeCallbacks(updateProgressAction)
        val playbackState = player?.playbackState ?: Player.STATE_IDLE
        if (playbackState != Player.STATE_IDLE && playbackState != Player.STATE_ENDED) {
            var delayMs: Long
            if (player!!.playWhenReady && playbackState == Player.STATE_READY) {
                delayMs = 1000 - position % 1000
                if (delayMs < 200) {
                    delayMs += 1000
                }
            } else {
                delayMs = 1000
            }

            //check to display ad
            /*if (ad - 3000 <= position && position <= ad && !check) {
                check = true
                initAd()
            }*/
            handler!!.postDelayed(updateProgressAction, delayMs)
        }
    }


    fun lockScreen(lock: Boolean) {

        val sec_mid = findViewById<LinearLayout>(com.example.moviestreamingapp.R.id.sec_controlvid1)
        val sec_bottom =
            findViewById<LinearLayout>(com.example.moviestreamingapp.R.id.sec_controlvid2)
        if (lock) {
            sec_mid.visibility = View.INVISIBLE
            sec_bottom.visibility = View.INVISIBLE
        } else {
            sec_mid.visibility = View.VISIBLE
            sec_bottom.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {

        if (isLock) return

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            bt_fullscreen!!.performClick()
        } else super.onBackPressed()
    }

    override fun onStop() {
        super.onStop()
        exoPlayer!!.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        exoPlayer!!.release()
    }

    override fun onPause() {
        super.onPause()
        exoPlayer!!.pause()
    }


    private fun StreamMovie() {
        val intent = Intent(this, MovieStreamActivity::class.java)
        intent.putExtra("movie_id", imdbId)
        startActivity(intent)
    }
}