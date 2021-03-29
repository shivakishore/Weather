package com.weatherapp.view

import android.annotation.SuppressLint
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.weatherapp.R
import com.weatherapp.WeatherViewModel
import com.weatherapp.model.valueobject.Bookmark
import kotlinx.android.synthetic.main.fragment_map.*
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private var marker: Marker? = null
    private lateinit var mMap: GoogleMap
    private lateinit var mapViews: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var viewModel: WeatherViewModel
    private lateinit var snackbar: Snackbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMapClickListener(this)
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location ->
                showMarker(LatLng(location.latitude, location.longitude))

            }
    }

    private fun showMarker(latLng: LatLng) {
        val geocoder = Geocoder(activity, Locale.getDefault())
        val addresses: List<Address> =
            geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
        if (addresses.isEmpty() || (addresses[0].locality == null && addresses[0].subAdminArea == null)) {
            Toast.makeText(context, "Could not locate this place", Toast.LENGTH_LONG).show()
            return
        }
        var city: String? = addresses[0].locality
        if (city == null) {
            city = addresses[0].subAdminArea
        }
        marker?.remove()
        marker = mMap.addMarker(MarkerOptions().position(latLng).title(city))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(latLng, 9f)
        )
        snackbar =
            Snackbar.make(mapView, city!!, Snackbar.LENGTH_INDEFINITE).setAction(R.string.select) {
                viewModel.addBookMark.value = Bookmark(city, latLng.latitude, latLng.longitude)
                findNavController().navigate(R.id.action_mapFragment_to_homeFragment)
            }
        snackbar.animationMode = Snackbar.ANIMATION_MODE_SLIDE
        snackbar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        mapViews = mapView
        mapViews.onCreate(savedInstanceState)
        mapViews.onResume()
        try {
            MapsInitializer.initialize(activity?.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mapViews.getMapAsync(this)
    }

    override fun onResume() {
        super.onResume()
        mapViews.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapViews.onPause()
        snackbar.dismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapViews.onDestroy()
        snackbar.dismiss()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapViews.onLowMemory()
    }

    override fun onMapClick(latLng: LatLng) {
        showMarker(latLng)
    }
}