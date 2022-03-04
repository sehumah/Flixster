package com.assignment1.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log

private const val TAG = "MovieDetailActivity"
class MovieDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        // Get movie object out of intent's putExtra
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie  // cast as non-nullable Movie because by default it's going to be nullable movie but we know that based on how the app is designed, we should always have a valid movie extra in the intent
        Log.i(TAG, "Movie is $movie")

    }
}
