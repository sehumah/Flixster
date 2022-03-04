package com.assignment1.flixster

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

const val MOVIE_EXTRA = "MOVIE_EXTRA"
private const val TAG = "MovieAdapter"

class MovieAdapter(private val context: Context, private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    // ViewHolder is already defined by the RecyclerView so we're just extending it
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        // get references to the individual components in the itemView i.e the 2 TextViews & the ImageView & populate it with the correct data in the movie
        private val ivPoster = itemView.findViewById<ImageView>(R.id.iv_poster)
        private val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        private val tvOverview = itemView.findViewById<TextView>(R.id.tv_overview)
        private val tvReleaseDate = itemView.findViewById<TextView>(R.id.tv_release_date)

        // Register the click listener
        init {
            itemView.setOnClickListener(this)
        }

        // bind movie data to the correct ItemViews
        fun bind(movie: Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            tvReleaseDate.text = movie.releaseDate

            // change image based on phone screen orientation
            var imageURL = ""
            val orientation = context.resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                imageURL = movie.posterImageURL
            }
            else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.backdropPathURL
            }
            // load posterImageURL or backdropPathURL into ivPoster ImageView
            Glide
                .with(context)
                .load(imageURL)
//                .placeholder(R.drawable.ic_launcher_background)
                .into(ivPoster)
        }

        override fun onClick(v: View?) {
            // 1. Get notified of movie that was clicked
            val movie = movies[adapterPosition]  // movie tapped
            Toast.makeText(context, movie.title, Toast.LENGTH_SHORT).show()  // notify with the movie's title when a movie is clicked

            // 2. Use the Intent system to navigate to a new screen/activity
            val intent = Intent(context, MovieDetailActivity::class.java)
            intent.putExtra(MOVIE_EXTRA, movie)
            context.startActivity(intent)
        }
    }

    // expensive operation: creating a view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    // cheap operation: simply bind data to an existing ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {  // given a ViewHolder & a position, take the data at that position & bind it into the ViewHolder
        Log.i(TAG, "onBindViewHolder, position: $position")  // indicate the position that we're binding here
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }
}
