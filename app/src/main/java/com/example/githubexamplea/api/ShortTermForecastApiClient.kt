package com.example.githubexamplea.api

import com.example.githubexamplea.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object OpenWeatherApiClient {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    // HTTP 요청 및 응답을 로깅하는 인터셉터 (디버깅 용도)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // OkHttpClient 설정 (로깅 인터셉터 추가)
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Gson 설정 (JSON 파싱 시 유연한 처리)
    private val gson = GsonBuilder()
        .setLenient()
        .create()

    // Retrofit 인스턴스 생성 (싱글턴)
    val service: OpenWeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpenWeatherApiService::class.java)
    }
}
