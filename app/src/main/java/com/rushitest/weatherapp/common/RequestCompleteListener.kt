package com.rushitest.weatherapp.common

import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie

interface RequestCompleteListener<T> {
    fun onRequestSuccess(data: T)
    fun onRequestFailed(errorMessage: String)
}

interface RequestCompleteListenerMovie<T> {
    fun onRequestSuccess(data: List<Movie>?)
    fun onRequestFailed(errorMessage: String)
}

