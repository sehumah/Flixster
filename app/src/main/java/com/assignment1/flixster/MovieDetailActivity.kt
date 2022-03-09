package com.assignment1.flixster

import android.os.Bundle
import android.util.Log
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

private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"
private const val YOUTUBE_API_KEY = R.string.YOUTUBE_API_KEY.toString()
private const val TAG = "MovieDetailActivity"
class MovieDetailActivity : YouTubeBaseActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var rbRatingBar: RatingBar
    private lateinit var tvOverview: TextView
    private lateinit var tvReleaseDate: TextView
    private lateinit var tvVoteCount: TextView
    private lateinit var tvVoteAverage: TextView
    private lateinit var tvPopularity: TextView
    private lateinit var tvOriginalLanguage: TextView
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        tvTitle = findViewById(R.id.tv_title)
        rbRatingBar = findViewById(R.id.rb_vote_average)
        tvOverview = findViewById(R.id.tv_overview)
        tvReleaseDate = findViewById(R.id.tv_release_date)
        tvVoteCount = findViewById(R.id.tv_vote_count)
        tvVoteAverage = findViewById(R.id.tv_vote_average)
        tvPopularity = findViewById(R.id.tv_popularity)
        tvOriginalLanguage = findViewById(R.id.tv_original_language)
        ytPlayerView = findViewById(R.id.ypv_yt_player)

        // Get movie object out of intent's putExtra
        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie  // cast as non-nullable Movie because by default it's going to be nullable movie but we know that based on how the app is designed, we should always have a valid movie extra in the intent
        Log.i(TAG, "Movie is $movie")

        // now, take data from the movie object and put it on the new screen
        tvTitle.text = movie.title
        rbRatingBar.rating = movie.voteAverageRB.toFloat()
        tvOverview.text = movie.overview
        tvReleaseDate.text = movie.releaseDate
        tvVoteCount.text = movie.voteCount
        tvVoteAverage.text = movie.voteAverage
        tvPopularity.text = movie.popularity.toString()
        tvOriginalLanguage.text = movie.originalLanguage

        // make get request to moviesDB for movie trailers
        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.id), object : JsonHttpResponseHandler() {

            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
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
                        val movieTrailerJSON = trailersArray.getJSONObject(0)
                        val youtubeKey = movieTrailerJSON.getString("key")
                        // now, play youtube video with this trailer
                        initializeYouTube(youtubeKey)
                    }
                } catch (e: JSONException) {
                        Log.i(TAG, "Encountered an exception: $e")
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
