package com.example.githubexamplea.api

import com.example.githubexamplea.api.model.ForecastResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// OpenWeather의 5일간 3시간 단위 예보 API 호출
interface OpenWeatherApiService {
    @GET("forecast")
    suspend fun getWeatherForecast(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "kr"
    ): Response<ForecastResponse>
}
