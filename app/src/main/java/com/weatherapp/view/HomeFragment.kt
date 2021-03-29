package com.weatherapp.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.weatherapp.R
import com.weatherapp.WeatherViewModel
import com.weatherapp.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var viewModel: WeatherViewModel
    private lateinit var homeBinding: FragmentHomeBinding
    private lateinit var bookmarksAdapter: BookmarksAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_home,
            container,
            false
        )

        return homeBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(WeatherViewModel::class.java)
        homeBinding.viewModel = viewModel
        bookmarksAdapter = BookmarksAdapter(viewModel)
        rv_bookmarklist.apply {
            adapter = bookmarksAdapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        CoroutineScope(Dispatchers.IO).launch {
            val bookmarkList = viewModel.getAllBookMarks()
            withContext(Main) {
                if (bookmarkList.isEmpty()) {
                    emptytext.visibility = View.VISIBLE
                    rv_bookmarklist.visibility = View.GONE
                } else {
                    emptytext.visibility = View.GONE
                    rv_bookmarklist.visibility = View.VISIBLE
                    bookmarksAdapter.setBookmarks(bookmarkList)
                }
            }
        }
        viewModel.pickLocationClicked.observe(viewLifecycleOwner, Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                if (it == null) return@Observer
                if (checkLocationPermission())
                    findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
                viewModel.pickLocationClicked.value = null
            }
        })
        viewModel.addBookMark.observe(viewLifecycleOwner, Observer {
            if (it == null) return@Observer
            CoroutineScope(Dispatchers.IO).launch {
                val ret = viewModel.addBookMarktoDB(it)
                withContext(Main) {
                    bookmarksAdapter.addBookmark(it)
                    Toast.makeText(context, it.name, Toast.LENGTH_LONG).show()
                    viewModel.addBookMark.value = null
                }
            }
        })
        viewModel.detailedViewClick.observe(viewLifecycleOwner, Observer {
            if (viewLifecycleOwner.lifecycle.currentState == Lifecycle.State.RESUMED) {
                findNavController().navigate(R.id.action_homeFragment_to_cityFragment)
            }
        })
        viewModel.bookmarksChanged.observe(viewLifecycleOwner, Observer {
            if (bookmarksAdapter.bookmarks.isEmpty()) {
                emptytext.visibility = View.VISIBLE
                rv_bookmarklist.visibility = View.GONE
            } else {
                emptytext.visibility = View.GONE
                rv_bookmarklist.visibility = View.VISIBLE
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                999
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            999 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findNavController().navigate(R.id.action_homeFragment_to_mapFragment)
            } else {
                Toast.makeText(
                    context,
                    "Cannot add city without location permission",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
