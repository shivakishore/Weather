package com.weatherapp

import android.app.Application
import com.weatherapp.model.BookmarkRepository

/**
 * Created by Shiva Kishore on 3/8/2021.
 */
class Application: Application() {
    override fun onCreate() {
        super.onCreate()
        BookmarkRepository.getBookmarkRepository(applicationContext)
    }
}