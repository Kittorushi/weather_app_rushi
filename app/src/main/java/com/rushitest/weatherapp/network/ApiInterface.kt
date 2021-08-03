package com.rushitest.weatherapp.network

import com.rushitest.weatherapp.features.weather_info_show.model.data_class.WeatherInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun callApiForWeatherInfo(@Query("q") cityId: String): Call<WeatherInfoResponse>
}