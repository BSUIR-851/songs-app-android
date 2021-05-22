package com.morozov.songs.ui.main.googleMaps

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.morozov.songs.R
import com.morozov.songs.services.SessionService


class GoogleMapsFragment : Fragment() {

    private lateinit var mapFragment: SupportMapFragment
    private lateinit var googleMap: GoogleMap
    private var mapIsReady = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_google_maps, container, false)
        initViewObjects(view)
        setupViewObjects()
        return view
    }

    private fun initViewObjects(view: View) {
        mapFragment = childFragmentManager.findFragmentById(R.id.fragment_google_maps_fragment_google_maps) as SupportMapFragment
    }

    private fun setupViewObjects() {
        setupButtonListeners()

        mapFragment.getMapAsync { googleMap ->
            this.googleMap = googleMap
            mapIsReady = true
            updateMarkers()
        }
    }

    private fun setupButtonListeners() {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finishAffinity()
        }
    }

    private fun updateMarkers() {
        SessionService.getLocalAssets()?.forEach { asset ->
            val release = asset.release
            if (release != null) {
                val pos = LatLng(release.latitude, release.longitude)
                googleMap.addMarker(MarkerOptions().position(pos).title(asset.name).snippet(release.note))
            }
        }
    }

}