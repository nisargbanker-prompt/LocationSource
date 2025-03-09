package com.locationsource.devstree.ui.dashboard.navigation.addPlace.data

import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import com.locationsource.devstree.LocationSourceApplication
import com.locationsource.devstree.R
import com.locationsource.devstree.data.local.entity.Places
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddPlacesViewModel @Inject constructor(
    val context: LocationSourceApplication,
    val repository: AddPlacesRepository
) : ViewModel() {

    public val _buttonSaveState = MutableLiveData<Boolean>(false)
    public val buttonSaveState: LiveData<Boolean> = _buttonSaveState

    var primaryLocation: Places? = null
    val places = Places()
    public var isUpdate = ObservableField<Boolean>(false)

    init {
        primaryLocation = isPrimaryAddressExist()
    }

    fun saveButtonClickListner(view: View) {
        viewModelScope.launch {
            if (isUpdate.get()!!) {
                async { updatePlaceData(places) }.await()
            } else {
                async { insertPlaceData(places) }.await()
            }
            view.findNavController().popBackStack()
        }
    }

    fun isPrimaryAddressExist(): Places {
        return repository.isPrimaryAddressExist()
    }

    suspend fun insertPlaceData(place: Places) {
        return repository.insertPlaceData(place)
    }

    suspend fun updatePlaceData(place: Places) {
        return repository.updatePlaceData(place)
    }

    fun getDistanceInKilometers(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        return repository.getDistanceInKilometers(lat1, lon1, lat2, lon2)
    }

}