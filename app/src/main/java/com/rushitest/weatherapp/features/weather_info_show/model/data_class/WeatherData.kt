package com.rushitest.weatherapp.features.weather_info_show.model.data_class

/**
 * this class will be used in UI to show weather data
 */
data class WeatherData(
        var dateTime: String = "",
        var temperature: String = "0",
        var cityAndCountry: String = "",
        var weatherConditionIconUrl: String = "",
        var weatherConditionIconDescription: String = "",
        var humidity: String = "",
        var pressure: String = "",
        var visibility: String = "",
        var sunrise: String = "",
        var sunset: String = "",
        var tempMin: String = "",
        var tempMax: String = "",
        var windspedd: String = ""


)