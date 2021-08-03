package com.rushitest.weatherapp.features.weather_info_show.model

import android.content.Context

import com.rushitest.weatherapp.common.RequestCompleteListener
import com.rushitest.weatherapp.common.RequestCompleteListenerMovie
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.WeatherInfoResponse
import com.rushitest.weatherapp.network.ApiInterface
import com.rushitest.weatherapp.network.ApiInterfaceMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject


class WeatherInfoShowModelImpl @Inject constructor(
    private val context: Context,
    private val apiInterface: ApiInterface,
    private val apiInterfaceMovie:  ApiInterfaceMovie,
) : WeatherInfoShowModel {


    override fun getWeatherInfo(
        cityId: String,
        callback: RequestCompleteListener<WeatherInfoResponse>
    ) {

        val call: Call<WeatherInfoResponse> = apiInterface.callApiForWeatherInfo(cityId)

        call.enqueue(object : Callback<WeatherInfoResponse> {

            // if retrofit network call success, this method will be executed
            override fun onResponse(
                call: Call<WeatherInfoResponse>,
                response: Response<WeatherInfoResponse>
            ) {
                if (response.body() != null)
                    callback.onRequestSuccess(requireNotNull(response.body())) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<WeatherInfoResponse>, t: Throwable) {
                callback.onRequestFailed(requireNotNull(t.localizedMessage)) //let presenter know about failure
            }
        })
    }


    override fun getMovies(callback: RequestCompleteListenerMovie<Movie>) {
        val callMovie: Call<List<Movie>> = apiInterfaceMovie.getMovies()

        // if retrofit network call success, this method will be executed
        callMovie.enqueue(object :Callback<List<Movie>>{
            override fun onResponse(call: Call<List<Movie>>, response: Response<List<Movie>>) {
                if (response.body() != null)
                    callback.onRequestSuccess(requireNotNull(response.body())) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            override fun onFailure(call: Call<List<Movie>>, t: Throwable) {
                callback.onRequestFailed(requireNotNull(t.localizedMessage)) //let presenter know about failure
            }
        })
    }
}