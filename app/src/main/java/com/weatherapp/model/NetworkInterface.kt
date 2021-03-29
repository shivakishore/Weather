package com.weatherapp.model

import com.weatherapp.model.valueobject.Forecast
import com.weatherapp.model.valueobject.WeatherData
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface NetworkInterface {

    @POST("data/2.5/weather")
    suspend fun getTodayForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("units") units: String,
        @Query("appid") api: String
    ): Response<WeatherData>

    @POST("data/2.5/forecast?units=metric")
    suspend fun getForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") api: String
    ): Response<Forecast>

}