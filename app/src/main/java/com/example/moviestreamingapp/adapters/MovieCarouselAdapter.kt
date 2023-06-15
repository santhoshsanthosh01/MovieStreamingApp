package com.example.moviestreamingapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

import com.example.moviestreamingapp.activities.MovieDetailsActivity

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.moviestreamingapp.R
import com.example.moviestreamingapp.network.movie.MovieBrief
import com.example.moviestreamingapp.utils.Constants
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.String

class MovieCarouselAdapter(mMovies: List<MovieBrief>, mContext: Context) :
    RecyclerView.Adapter<MovieCarouselAdapter.MovieViewHolder>() {
    private val mMovies: List<MovieBrief>
    private val mContext: Context

    init {
        this.mMovies = mMovies
        this.mContext = mContext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(mContext).inflate(R.layout.carousel_single_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(mContext.applicationContext)
            .load(Constants.IMAGE_LOADING_BASE_URL_780 + mMovies[position].getBackdropPath())
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.movie_imageView)
        if (mMovies[position].getTitle() != null) holder.movie_title.setText(mMovies[position].getTitle()) else holder.movie_title.text =
            ""
        if (mMovies[position].getPopularity() != null) holder.movie_rating.text =
            String.format(
                "%.1f",
                mMovies[position].getVoteAverage()
            ) else holder.movie_rating.text =
            ""

//        holder.movie_counter.setText(position + 1 + "/" + mMovies.size());
    }

    override fun getItemCount(): Int {
        return mMovies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var movie_imageView: ImageView
        var movie_title: TextView
        var movie_rating: TextView
        var movie_cardView: CardView
        var movie_fab: FloatingActionButton

        //        public TextView movie_counter;
        init {
            movie_imageView = itemView.findViewById(R.id.carousel_imageView)
            movie_title = itemView.findViewById(R.id.carousel_title)
            movie_rating = itemView.findViewById(R.id.carousel_rating)
            movie_cardView = itemView.findViewById(R.id.carousel_main_card)
            movie_fab = itemView.findViewById(R.id.carousel_play_btn)
            //            movie_counter = itemView.findViewById(R.id.carousel_counter);
            movie_title.layoutParams.width =
                (mContext.resources.displayMetrics.widthPixels * 0.7).toInt()
            movie_cardView.setOnClickListener {
                val intent = Intent(mContext, MovieDetailsActivity::class.java)
                intent.putExtra("movie_id", mMovies[adapterPosition].getId())
                mContext.startActivity(intent)
            }
            movie_fab.setOnClickListener {
                val intent = Intent(mContext, MovieDetailsActivity::class.java)
                intent.putExtra("movie_id", mMovies[adapterPosition].getId())
                mContext.startActivity(intent)
            }
        }
    }
}