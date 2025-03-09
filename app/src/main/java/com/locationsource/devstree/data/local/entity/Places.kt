package com.locationsource.devstree.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Places(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var cityName: String? = null,
    var distance: String? = null,
    var lat: String? = null,
    var lng: String? = null,
    var isPrimary: Int = 0
) : Serializable


