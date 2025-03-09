package com.locationsource.devstree.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.locationsource.devstree.data.local.entity.Places

@Dao
interface PlacesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(place: Places)

    @Update
    suspend fun update(place: Places)

    @Query("Select * from Places")
    fun getPlaces(): List<Places>

    @Query("Select * from Places where isPrimary = 1")
    fun isPrimaryAddressExist(): Places

    @Query("Delete from Places where id = :id")
    fun deletePlace(id: Int)

    @Query("select * from Places order by CAST(distance as DOUBLE) asc")
    fun getAscendingPlaces(): List<Places>

    @Query("select * from Places order by CAST(distance as DOUBLE) desc")
    fun getDescendingPlaces(): List<Places>

}