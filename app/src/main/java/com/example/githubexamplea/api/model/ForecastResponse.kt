package com.example.githubexamplea.api.model

import com.google.gson.annotations.SerializedName

// 전체 날씨 예보 응답 데이터 모델
data class ForecastResponse(
    @SerializedName("list") val list: List<ForecastItem>,
    @SerializedName("city") val city: City
)

// 개별 예보 데이터
data class ForecastItem(
    @SerializedName("dt_txt") val dateTime: String,  // 날짜/시간 (YYYY-MM-DD HH:mm:ss)
    @SerializedName("main") val main: Main,
    @SerializedName("weather") val weather: List<Weather>,
    @SerializedName("wind") val wind: Wind
)

// 기온, 체감온도, 습도 관련 데이터
data class Main(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feels_like") val feelsLike: Float,
    @SerializedName("humidity") val humidity: Int
)

// 날씨 상태 (맑음, 흐림 등)
data class Weather(
    @SerializedName("description") val description: String
)

// 바람 속도 정보
data class Wind(
    @SerializedName("speed") val speed: Float
)

// 도시 정보
data class City(
    @SerializedName("name") val cityName: String
)
