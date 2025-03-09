package com.locationsource.devstree.ui.dashboard.navigation.places.data

import com.locationsource.devstree.data.local.AppDatabase
import com.locationsource.devstree.data.local.dao.PlacesDao
import com.locationsource.devstree.data.local.entity.Places
import javax.inject.Inject

class PlacesRepository @Inject constructor(
    val appDatabase: AppDatabase
) {

    var placesDao: PlacesDao = appDatabase.placesDao()

    fun getAllPalces(): List<Places> {
        return placesDao.getPlaces()
    }

    fun getAllPalcesAsc(): List<Places> {
        return placesDao.getAscendingPlaces()
    }

    fun getAllPalcesDesc(): List<Places> {
        return placesDao.getDescendingPlaces()
    }

    fun deletePlace(id: Int){
        placesDao.deletePlace(id)
    }

}