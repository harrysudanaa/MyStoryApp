package com.example.mystoryapp.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.example.mystoryapp.R

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.mystoryapp.databinding.ActivityMapsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val mapsViewModel by viewModels<MapsViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

     binding = ActivityMapsBinding.inflate(layoutInflater)
     setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        with (mapsViewModel) {
            getSession().observe(this@MapsActivity) { user ->
                getStoriesWithLocation("Bearer ${user.token}")
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mapsViewModel.location.observe(this) { location ->
            location.forEach { data ->
                val latLng = data.lat?.let { lat ->
                    data.lon?.let { lon -> LatLng(lat, lon) }
                }
                mMap.addMarker(
                    MarkerOptions()
                        .position(latLng!!)
                        .title(data.name)
                        .snippet(data.description)
                )
            }
        }
    }
}