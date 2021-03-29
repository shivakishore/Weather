package com.weatherapp.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.weatherapp.Config
import com.weatherapp.R
import com.weatherapp.Utils
import com.weatherapp.WeatherViewModel
import com.weatherapp.model.valueobject.Lists
import kotlinx.android.synthetic.main.fragment_city.*

/**
 * Created by Shiva Kishore on 3/8/2021.
 */
class ForecastAdapter(val context: Context, val lists: List<Lists>?) :
        RecyclerView.Adapter<ForecastAdapter.ForecastHolder>() {
//    private val lists = ArrayList<Lists>()

    inner class ForecastHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var date: TextView = itemView.findViewById(R.id.forecastDate)
        var temp: TextView = itemView.findViewById(R.id.forecastTemp)
        var feelsLike: TextView = itemView.findViewById(R.id.forecastFeelsLike)
        var description: TextView = itemView.findViewById(R.id.forecastDescription)
        var minMax: TextView = itemView.findViewById(R.id.forecastMinMax)
        var humid: TextView = itemView.findViewById(R.id.forecastHumid)
        var wind: TextView = itemView.findViewById(R.id.forecastWind)
        var icon: ImageView = itemView.findViewById(R.id.forecastIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastHolder {
        return ForecastHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return lists!!.size
    }

    override fun onBindViewHolder(holder: ForecastHolder, position: Int) {
        val forecast = lists?.get(position) ?: return
        holder.date.text = Utils.getDateStr(forecast.dt, Config.TIME_FORMAT_FORECAST)
        holder.temp.text = String.format(context.getString(R.string.temperature),forecast.main.temp.toInt().toString())
        holder.description.text = forecast.weather.get(0).description.capitalize()
        holder.feelsLike.text = String.format(context.getString(R.string.feels_like),forecast.main.feels_like.toInt().toString())
        holder.minMax.text = String.format(context.getString(R.string.minMaxTemp),forecast.main.temp_min.toInt().toString(),forecast.main.temp_max.toInt().toString())
        holder.humid.text = "Humidity ${forecast.main.humidity}%"
        holder.wind.text = "Wind ${forecast.wind.deg}°, ${forecast.wind.speed.toInt()} m/s"
        Glide.with(holder.icon.context).load("http://openweathermap.org/img/w/${forecast.weather.get(0).icon}.png").into(holder.icon)
    }
}