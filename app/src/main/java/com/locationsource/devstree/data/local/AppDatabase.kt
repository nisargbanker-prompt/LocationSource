package com.locationsource.devstree.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.locationsource.devstree.data.local.dao.PlacesDao
import com.locationsource.devstree.data.local.entity.Places

@Database(
    entities = [Places::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun placesDao(): PlacesDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also {
                    instance = it
                }
            }

        private fun buildDatabase(appContext: Context) =
            Room.databaseBuilder(appContext, AppDatabase::class.java, "LocationSource")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
    }

}