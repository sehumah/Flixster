package com.assignment1.flixster

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException


private const val TAG = "MainActivity"
private const val URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MainActivity : AppCompatActivity() {
    private val movies = mutableListOf<Movie>()
    private lateinit var rvMovies: RecyclerView

    /**
     * Steps to add a RecyclerView
     * 1. Define a data model class as the data source
     * 2. Add a RecyclerView to the layout
     * 3. Create a custom row layout XML file to visualize the item
     * 4. Create an Adapter and ViewHolder to render the item
     * 5. Bind the Adapter to the data source to populate the RecyclerView
     * 6. Bind a layout manager to the RecyclerView
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMovies = findViewById(R.id.rv_movies)  // get reference to the RecyclerView
        val movieAdapter = MovieAdapter(this, movies)  // get reference to MovieAdapter
        rvMovies.adapter = movieAdapter  // bind movieAdapter to be the Adapter of the RecyclerView
        rvMovies.layoutManager = LinearLayoutManager(this)

        val client = AsyncHttpClient()
        client.get(URL, object: JsonHttpResponseHandler () {
            override fun onFailure(statusCode: Int, headers: Headers?, response: String?, throwable: Throwable?) {
                Log.e(TAG, "onFailure $statusCode")  // log error message
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess: JSON data $json")
                /* handle unexpected errors */
                try {
                    val moviesJSONArray = json.jsonObject.getJSONArray("results")
                    movies.addAll(Movie.fromJSONArray(moviesJSONArray))
                    movieAdapter.notifyDataSetChanged()  // notify the adapter that the underlying data has changed
                    Log.i(TAG, "Movie list: $movies")  // log out parsed movies object
                } catch (e: JSONException) {
                    Log.e(TAG, "Encountered an exception: $e")
                }
            }
        })
    }
}
