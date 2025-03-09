package com.locationsource.devstree.ui.dashboard.navigation.addPlace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.locationsource.devstree.R
import com.locationsource.devstree.databinding.FragmentAddPlacesBinding
import com.locationsource.devstree.ui.dashboard.navigation.addPlace.data.AddPlacesViewModel
import com.locationsource.devstree.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddPlacesFragment : Fragment() {

    lateinit var binding: FragmentAddPlacesBinding
    private lateinit var mMap: GoogleMap
    private val viewModel: AddPlacesViewModel by viewModels()

    private var id: Int = 0
    private var lat: String = ""
    private var lng: String = ""
    private var name: String = ""

    private val callback = OnMapReadyCallback { googleMap ->
        mMap = googleMap

        if (lat.isEmpty()) {
            val india = LatLng(20.5937, 78.9629) // Coordinates for India
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    india,
                    5f
                )
            ) // Zoom level 5 shows India nicely
        } else {
            viewModel.isUpdate.set(true)
            val india = LatLng(lat.toDouble(), lng.toDouble()) // Coordinates for India
            googleMap.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    india,
                    15f
                )
            )
            googleMap.addMarker(MarkerOptions().position(india).title(name))
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddPlacesBinding.inflate(layoutInflater)

        if (!arguments?.isEmpty!!) {
            lat = arguments?.getString(Constants.LAT)!!
            lng = arguments?.getString(Constants.LNG)!!
            name = arguments?.getString(Constants.NAME)!!
            id = arguments?.getInt(Constants.ID)!!
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        // Initialize the Places API
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.google_maps_key))
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // Set up AutocompleteSupportFragment
        val autocompleteFragment =
            childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            listOf(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.LAT_LNG
            )
        )

        // Set up listener for place selection
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                // Show place details and move the map camera
                val latLng = place.latLng
                if (latLng != null) {
                    //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                    mMap.addMarker(MarkerOptions().position(latLng).title(place.displayName))
                    viewModel._buttonSaveState.postValue(true)

                    viewModel.places.cityName = place.displayName
                    viewModel.places.lat = latLng.latitude.toString()
                    viewModel.places.lng = latLng.longitude.toString()
                    if (viewModel.primaryLocation != null) {
                        viewModel.places.isPrimary = 0
                        viewModel.places.distance = viewModel.getDistanceInKilometers(
                            viewModel.primaryLocation!!.lat!!.toDouble(),
                            viewModel.primaryLocation!!.lng!!.toDouble(),
                            latLng.latitude,
                            latLng.longitude
                        )
                            .toString()
                    } else {
                        viewModel.places.isPrimary = 1
                        viewModel.places.distance = "0"
                    }
                    if (viewModel.isUpdate.get()!!) {
                        viewModel.places.id = id
                    }

                }
            }

            override fun onError(status: com.google.android.gms.common.api.Status) {
                Toast.makeText(
                    requireContext(),
                    "Error: ${status.statusMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })

    }


}