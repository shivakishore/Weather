package com.weatherapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.weatherapp.Config
import com.weatherapp.R
import com.weatherapp.Utils
import com.weatherapp.WeatherViewModel
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.fragment_city.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import java.util.*

class CityFragment : Fragment() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var adapter: ForecastAdapter
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_city, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        viewModel.detailedViewClick.observe(viewLifecycleOwner, Observer {
//            locationName.text = it.name
            toolbar_layout.title = it.name

            CoroutineScope(IO).launch {
                val result = viewModel.getWeather(LatLng(it.lat, it.lon))
                launch(Main) {
                    if (result.isSuccessful) {
                        val main = result.body()?.main
                        val wind = result.body()?.wind
                        val sys = result.body()?.sys
                        description.text = result.body()?.weather?.get(0)?.description?.capitalize()
                        mainTemp.text = String.format(getString(R.string.temperature),main?.temp?.toInt().toString())
                        feelsLike.text = String.format(getString(R.string.feels_like),main?.feels_like?.toInt().toString())
                        pressureHumid.text = String.format(getString(R.string.pressureHumid),main?.pressure.toString(),main?.humidity.toString())
                        minMaxTemp.text = String.format(getString(R.string.minMaxTemp),main?.temp_min?.toInt().toString(),main?.temp_max?.toInt().toString())
                        windSpeed.text = String.format(getString(R.string.wind_speed),wind?.speed?.toString())
                        windAngle.text = String.format(getString(R.string.wind_deg),wind?.deg?.toString())
                        sunrise.text = String.format(getString(R.string.sunrise),sys?.sunrise?.let { it1 -> Utils.getDateStr(it1, Config.TIME_FORMAT)})
                        sunset.text = String.format(getString(R.string.sunset),sys?.sunset?.let { it1 -> Utils.getDateStr(it1, Config.TIME_FORMAT)})
                    } else {
                        description.text = "error: ${result.errorBody().toString()}"
                    }
                }
            }
            CoroutineScope(IO).launch {
                val result = viewModel.getForecast(LatLng(it.lat, it.lon))
                launch(Main) {
                    if (result.isSuccessful) {
                        rv_forecast.apply {
                            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                            this.adapter = ForecastAdapter(context,result.body()?.list)
                            adapter = this.adapter
                        }
                    } else {
                    }
                }
            }

        })

        activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        findNavController().popBackStack(R.id.homeFragment, false)
                    }
                })
    }
}