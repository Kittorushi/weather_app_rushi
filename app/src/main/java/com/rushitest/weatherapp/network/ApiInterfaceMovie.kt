package com.rushitest.weatherapp.network


import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie
import retrofit2.Call
import retrofit2.http.GET


// interface method to call
interface ApiInterfaceMovie {
    @GET("movielist.json")
    fun getMovies(): Call<List<Movie>>
}