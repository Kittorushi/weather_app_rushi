package com.rushitest.weatherapp.features.weather_info_show.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rushitest.weatherapp.features.weather_info_show.model.WeatherInfoShowModel
import javax.inject.Inject

class WeatherInfoViewModelFactory @Inject constructor(private val arg: WeatherInfoShowModel) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(WeatherInfoShowModel::class.java)
            .newInstance(arg)
    }
}