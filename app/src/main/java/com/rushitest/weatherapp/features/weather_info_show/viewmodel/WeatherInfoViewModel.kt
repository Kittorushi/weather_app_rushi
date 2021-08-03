package com.rushitest.weatherapp.features.weather_info_show.viewmodel


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rushitest.weatherapp.common.RequestCompleteListener
import com.rushitest.weatherapp.common.RequestCompleteListenerMovie
import com.rushitest.weatherapp.features.weather_info_show.model.WeatherInfoShowModel
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.Movie
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.WeatherData
import com.rushitest.weatherapp.features.weather_info_show.model.data_class.WeatherInfoResponse
import com.rushitest.weatherapp.utils.kelvinToCelsius
import com.rushitest.weatherapp.utils.unixTimestampToDateTimeString
import com.rushitest.weatherapp.utils.unixTimestampToTimeString
import javax.inject.Inject


class WeatherInfoViewModel @Inject constructor(var model: WeatherInfoShowModel) : ViewModel() {

    val weatherInfoLiveData = MutableLiveData<WeatherData>()
    val weatherInfoFailureLiveData = MutableLiveData<String>()



    /**We can inject the instance of Model in Constructor using dependency injection.
     */
    fun getWeatherInfo(cityId: String) {

        model.getWeatherInfo(cityId, object :
            RequestCompleteListener<WeatherInfoResponse> {
            override fun onRequestSuccess(data: WeatherInfoResponse) {

                // business logic and data manipulation tasks should be done here
                val weatherData = WeatherData(
                    dateTime = data.dt.unixTimestampToDateTimeString(),
                    temperature = data.main.temp.kelvinToCelsius().toString(),
                    cityAndCountry = "${data.name}, ${data.sys.country}",
                    weatherConditionIconUrl = "http://openweathermap.org/img/w/${data.weather[0].icon}.png",
                    weatherConditionIconDescription = data.weather[0].description,
                    humidity = "${data.main.humidity}%",
                    pressure = "${data.main.pressure} mBar",
                    visibility = "${data.visibility / 1000.0} KM",
                    sunrise = data.sys.sunrise.unixTimestampToTimeString(),
                    sunset = data.sys.sunset.unixTimestampToTimeString(),
                    tempMax = data.main.tempMax.kelvinToCelsius().toString(),
                    tempMin = data.main.tempMin.kelvinToCelsius().toString(),
                    windspedd = data.wind.speed.toString()

                )


                // After applying business logic and data manipulation, we push data to show on UI
                weatherInfoLiveData.postValue(weatherData) // PUSH data to LiveData object
            }

            override fun onRequestFailed(errorMessage: String) {
                weatherInfoFailureLiveData.postValue(errorMessage) // PUSH error message to LiveData object
            }
        })
    }



}