package com.weatherapp.model.valueobject

import com.google.gson.annotations.SerializedName

/**
 * Created by Shiva Kishore on 3/8/2021.
 */
data class Forecast(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<Lists>,
    val message: Int
)

data class City(
    val coord: Coord,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)

data class Lists(
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    @SerializedName("main")
    val main: Mainx,
    val pop: Double,
    val rain: Rain,
    @SerializedName("sys")
    val sys: Sysx,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)

data class Clouds(
    val all: Int
)

data class Mainx(
    val feels_like: Double,
    val grnd_level: Int,
    val humidity: Int,
    val pressure: Int,
    val sea_level: Int,
    val temp: Double,
    val temp_kf: Double,
    val temp_max: Double,
    val temp_min: Double
)

data class Rain(
    val `3h`: Double
)

data class Sysx(
    val pod: String
)

data class Wind(
    val deg: Int,
    val speed: Double
)