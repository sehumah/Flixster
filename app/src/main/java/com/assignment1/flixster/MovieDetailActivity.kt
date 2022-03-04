package com.assignment1.flixster

import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import org.w3c.dom.Text

private const val TAG = "MovieDetailActivity"
class MovieDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        tvTitle = findViewById(R.id.tv_title)
        tvOverview = findViewById(R.id.tv_overview)
        ratingBar = findViewById(R.id.rb_vote_average)

        // Get movie object out of intent's putExtra
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie  // cast as non-nullable Movie because by default it's going to be nullable movie but we know that based on how the app is designed, we should always have a valid movie extra in the intent
        Log.i(TAG, "Movie is $movie")

        // now, take data from the movie object and put it on the new screen
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

    }
}
