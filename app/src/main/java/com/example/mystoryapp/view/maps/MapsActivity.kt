package com.example.mystoryapp.view.maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
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

        with(mapsViewModel) {
            getSession().observe(this@MapsActivity) { user ->
                getStoriesWithLocation(getString(R.string.bearer_token, user.token))
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true

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
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLng(
                    LatLng(
                        location.first().lat!!,
                        location.first().lon!!
                    )
                )
            )
        }

        try {
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style
                )
            )

            if (!success) {
                Log.e(TAG, getString(R.string.style_parsing_failed))
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, getString(R.string.can_t_find_style_error), e)
        }
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}