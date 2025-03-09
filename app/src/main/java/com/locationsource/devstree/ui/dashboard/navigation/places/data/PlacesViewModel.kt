package com.locationsource.devstree.ui.dashboard.navigation.places.data

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import com.locationsource.devstree.LocationSourceApplication
import com.locationsource.devstree.R
import com.locationsource.devstree.data.local.entity.Places
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    val context: LocationSourceApplication,
    val repository: PlacesRepository
) : ViewModel() {

    fun addPOIClickListner(view : View) {
        view.findNavController().navigate(R.id.action_allPlacesFragment_to_addPlacesFragment)
    }

    fun addFABClickListner(view : View) {
        view.findNavController().navigate(R.id.action_allPlacesFragment_to_mapRouteFragment)
    }

    fun getAllPalces(): List<Places> {
        return repository.getAllPalces()
    }

    fun getAllPalcesAsc(): List<Places> {
        return repository.getAllPalcesAsc()
    }

    fun getAllPalcesDesc(): List<Places> {
        return repository.getAllPalcesDesc()
    }

    fun deletePlace(id : Int){
        repository.deletePlace(id)
    }

}