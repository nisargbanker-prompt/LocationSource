package com.locationsource.devstree.ui.dashboard.navigation.addPlace.data

import android.location.Location
import com.locationsource.devstree.data.local.AppDatabase
import com.locationsource.devstree.data.local.dao.PlacesDao
import com.locationsource.devstree.data.local.entity.Places
import javax.inject.Inject

class AddPlacesRepository @Inject constructor(
    val appDatabase: AppDatabase
) {
    var placesDao: PlacesDao = appDatabase.placesDao()

    suspend fun insertPlaceData(place: Places) {
        return placesDao.insert(place)
    }

    suspend fun updatePlaceData(place: Places) {
        return placesDao.update(place)
    }

    fun isPrimaryAddressExist() : Places{
        return placesDao.isPrimaryAddressExist()
    }

    fun getDistanceInKilometers(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val results = FloatArray(1)

        // Calculate the distance in meters between two geographical points
        Location.distanceBetween(lat1, lon1, lat2, lon2, results)

        // Convert the result to kilometers
        val distanceInMeters = results[0].toDouble()
        return String.format("%.2f", (distanceInMeters / 1000)).toDouble()  // Convert meters to kilometers
    }

}