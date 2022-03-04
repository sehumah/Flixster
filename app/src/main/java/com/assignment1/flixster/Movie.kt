package com.assignment1.flixster

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import org.json.JSONArray

/**
 A class for each single movie object that will be displayed in the UI
 */
@Parcelize
data class Movie (
    val id: Int,
    val title: String,
    val overview: String,
    val releaseDate: String,
    private val posterPath: String,
    private val backdropPath: String) : Parcelable {  // posterPath & backdropPath made private since no one needs to use it except posterImageURL

    @IgnoredOnParcel
    val posterImageURL = "https://image.tmdb.org/t/p/w342/$posterPath"
    @IgnoredOnParcel
    val backdropPathURL = "https://image.tmdb.org/t/p/w342/$backdropPath"

    companion object {
        fun fromJSONArray (moviesJSONArray: JSONArray): List<Movie> {  // iterate through json array and return a list of movie data classes
            val movies = mutableListOf<Movie>()

            // populate movies list
            for (i in 0 until moviesJSONArray.length()) {
                // grab the JSON object at that particular position
                val movieJSON = moviesJSONArray.getJSONObject(i)
                movies.add(
                    Movie (
                        movieJSON.getInt("id"),
                        movieJSON.getString("title"),
                        movieJSON.getString("overview"),
                        "Release date: " + movieJSON.getString("release_date"),
                        movieJSON.getString("poster_path"),
                        movieJSON.getString("backdrop_path")
                    )
                )
            }
            return movies
        }
    }
}
