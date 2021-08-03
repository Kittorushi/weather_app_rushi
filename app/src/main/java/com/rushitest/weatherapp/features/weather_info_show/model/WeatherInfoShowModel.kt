package com.rushitest.weatherapp.features.weather_info_show.model

import com.rushitest.weatherapp.common.RequestCompleteListener
import com.rushitest.weatherapp.common.RequestCompleteListenerMovie
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.WeatherInfoResponse

interface WeatherInfoShowModel {

    fun getWeatherInfo(cityId: String, callback: RequestCompleteListener<WeatherInfoResponse>)
    fun getMovies(callback: RequestCompleteListenerMovie<Movie>)
}