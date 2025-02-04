package com.example.githubexamplea.api.model

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItem>,
    @SerializedName("city") val city: City
)

data class ForecastItem(
    @SerializedName("dt_txt") val dateTime: String,  // 날짜/시간 (YYYY-MM-DD HH:mm:ss)
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind
)

data class Main(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("humidity") val humidity: Int
)

data class Weather(
    @SerializedName("description") val description: String
)

data class Wind(
    @SerializedName("speed") val speed: Float
)

data class City(
    @SerializedName("name") val cityName: String
)
