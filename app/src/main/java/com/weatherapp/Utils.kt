package com.weatherapp

import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {
        fun getDateStr(dateInMillis: Int, format: String): String {
            val formatter = SimpleDateFormat(format)
            return formatter.format(Date(dateInMillis * 1000.toLong()))
        }
    }
}