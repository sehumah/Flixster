package com.assignment1.flixster

import android.icu.text.CaseMap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.FilterQueryProvider
import android.widget.LinearLayout
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers
import org.json.JSONException
import org.w3c.dom.Text

private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val YOUTUBE_API_KEY = "AIzaSyB-9JgfcSGhDzeGCuPLWlNNClGxwTi7LF4"
private const val TAG = "MovieDetailActivity"
class MovieDetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvOverview: TextView
    private lateinit var ratingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        tvTitle = findViewById(R.id.tv_title)
        tvOverview = findViewById(R.id.tv_overview)
        ratingBar = findViewById(R.id.rb_vote_average)
        ytPlayerView = findViewById(R.id.ypv_yt_player)

        // Get movie object out of intent's putExtra
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie  // cast as non-nullable Movie because by default it's going to be nullable movie but we know that based on how the app is designed, we should always have a valid movie extra in the intent
        Log.i(TAG, "Movie is $movie")

        // now, take data from the movie object and put it on the new screen
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        ratingBar.rating = movie.voteAverage.toFloat()

        // make get request to moviesDB for movie trailers
        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.id), object : JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess $statusCode")
                try {
                    val trailersArray = json.jsonObject.getJSONArray("results")
                    if (trailersArray.length() == 0) {
                        Log.i(TAG, "No movie trailers found")  // warn developer
                        return
                    } else {
                        val movieTrailerJSON = trailersArray.getJSONObject(6)
                        val youtubeKey = movieTrailerJSON.getString("key")
                        // now, play youtube video with this trailer
                        initializeYouTube(youtubeKey)
                    }
                } catch (e: JSONException) {

                }
            }
        })
    }

    private fun initializeYouTube(youtubeKey: String) {
        ytPlayerView.initialize(YOUTUBE_API_KEY, object : YouTubePlayer.OnInitializedListener {
            override fun onInitializationSuccess (
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                player?.cueVideo(youtubeKey)  // play YouTube video
                Log.i(TAG, "onInitializationSuccess")
            }

            override fun onInitializationFailure (
                provider: YouTubePlayer.Provider?,
                ytInitResult: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure\tYouTubeInitializationResult: $ytInitResult")
            }
        })
    }
}
