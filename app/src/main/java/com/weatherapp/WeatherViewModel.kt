package com.weatherapp

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.model.BookmarkRepository
import com.weatherapp.model.RetrofitClient
import com.weatherapp.model.valueobject.Bookmark
import com.weatherapp.model.valueobject.Forecast
import com.weatherapp.model.valueobject.WeatherData
import kotlinx.coroutines.launch
import retrofit2.Response

class WeatherViewModel : ViewModel() {

    val detailedViewClick = MutableLiveData<Bookmark>()
    val bookmarksChanged = MutableLiveData<Boolean>()
    val pickLocationClicked = MutableLiveData<Boolean>()
    val addBookMark = MutableLiveData<Bookmark>()

    fun pickLocationClicked() {
        pickLocationClicked.value = true
    }

    suspend fun getAllBookMarks(): List<Bookmark> {
        return BookmarkRepository.getBookmarkRepository(null)?.bookmarksDao()?.getBookmarks()!!
    }

    suspend fun addBookMarktoDB(bookmark: Bookmark): Long? {
        return BookmarkRepository.getBookmarkRepository(null)?.bookmarksDao()
                ?.insertBookmark(bookmark)
    }

    fun removeBookmark(bookmark: Bookmark) {
        viewModelScope.launch {
            BookmarkRepository.getBookmarkRepository(null)?.bookmarksDao()?.deleteBookmark(bookmark)
        }
    }

    suspend fun getWeather(latLng: LatLng): Response<WeatherData> {
        return RetrofitClient.instance.getTodayForecast(
                latLng.latitude.toString(),
                latLng.longitude.toString(),
                "metric",
                Config.API_KEY
        )
    }

    suspend fun getForecast(latLng: LatLng): Response<Forecast> {
        return RetrofitClient.instance.getForecast(
                latLng.latitude.toString(),
                latLng.longitude.toString(),
                Config.API_KEY
        )
    }
}