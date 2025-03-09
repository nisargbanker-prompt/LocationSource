package com.locationsource.devstree.ui.dashboard.navigation.mapRoute

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.locationsource.devstree.R
import com.locationsource.devstree.data.local.entity.Places
import com.locationsource.devstree.ui.dashboard.navigation.places.data.PlacesViewModel
import com.tonyakitori.inc.easyroutes.EasyRoutesDirections
import com.tonyakitori.inc.easyroutes.EasyRoutesDrawer
import com.tonyakitori.inc.easyroutes.enums.TransportationMode
import com.tonyakitori.inc.easyroutes.extensions.drawRoute
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MapRouteFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    private val markersList: ArrayList<Marker> = arrayListOf()
    private val viewModel: PlacesViewModel by viewModels()
    private var placesList = ArrayList<Places>()
    private var wayPoitList = ArrayList<LatLng>()
    private var wayPointCityList = ArrayList<String>()

    private val callback = OnMapReadyCallback { googleMap ->

        mMap = googleMap

        placesList = viewModel.getAllPalcesAsc() as ArrayList

        placesList.forEachIndexed { index, places ->

            if (index != 0 && index != (placesList.size-1) ){
                val location = LatLng(places.lat!!.toDouble(), places.lng!!.toDouble())
                wayPoitList.add(location)
                wayPointCityList.add(places.cityName!!)
                mMap.addMarker(MarkerOptions().position(location).title(places.cityName))

            }

        }

        val placeDirections = EasyRoutesDirections(
            originPlace = placesList[0].cityName,
            destinationPlace = placesList[placesList.size-1].cityName,
            apiKey = getString(R.string.google_maps_key),
            waypointsLatLng = wayPoitList,
            waypointsPlaces = wayPointCityList,
            showDefaultMarkers= true,
            transportationMode = TransportationMode.DRIVING
        )

        val customPolylineOptions = PolylineOptions()
        customPolylineOptions.color(ContextCompat.getColor(requireActivity(), R.color.black))
        customPolylineOptions.width(15f)

        val routeDrawer = EasyRoutesDrawer.Builder(mMap)
            .pathWidth(10f)
            .pathColor(Color.BLUE)
            .geodesic(true)
            .previewMode(false)
            .build()

        mMap.drawRoute(
            context = requireActivity(),
            easyRoutesDirections = placeDirections,
            routeDrawer = routeDrawer,
            markersListCallback = {markers -> markersList.addAll(markers) },
            googleMapsLink = { url -> Log.d("GoogleLink", url)}
        ){ legs ->
            legs?.forEach {
                Log.d("Point startAddress:", it?.startAddress.toString())
                Log.d("Point endAddress:", it?.endAddress.toString())
                Log.d("Distance:", it?.distance.toString())
                Log.d("Duration:", it?.duration.toString())
            }
        }

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(placesList[0].lat!!.toDouble(), placesList[0].lng!!.toDouble())) // Set target location
            .zoom(10f) // Set zoom level (1-21)
            .bearing(0f) // Set bearing (rotation)
            .tilt(30f) // Set tilt angle (0 - 90)
            .build()


        // Animate the camera to the new position
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_map_route, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}