package com.weatherapp.model.valueobject

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

/**
 * Created by Shiva Kishore on 3/6/2021.
 */
@Parcelize
@Entity(tableName = "bookmarks")
data class Bookmark(@PrimaryKey var name: String, var lat: Double, var lon: Double) : Parcelable