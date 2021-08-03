package com.rushitest.weatherapp.features.weather_info_show.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rushitest.weatherapp.common.RequestCompleteListenerMovie
import com.rushitest.weatherapp.features.weather_info_show.model.WeatherInfoShowModel
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie
import javax.inject.Inject

class MovieViewModel @Inject constructor(var model: WeatherInfoShowModel) : ViewModel()  {


    val movieListLiveData = MutableLiveData<List<Movie>>()
    val movieListFailLiveData = MutableLiveData<String>()


    fun getMovies(){
        model.getMovies(object : RequestCompleteListenerMovie<Movie> {
            override fun onRequestFailed(errorMessage: String) {
                movieListFailLiveData.postValue(errorMessage)
            }

            override fun onRequestSuccess(data: List<Movie>?) {
                movieListLiveData.postValue(data)
            }
        })
    }
}