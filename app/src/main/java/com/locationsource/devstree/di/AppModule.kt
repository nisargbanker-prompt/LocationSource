package com.locationsource.devstree.di

import android.content.Context
import com.locationsource.devstree.LocationSourceApplication
import com.locationsource.devstree.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesApplicationContext(@ApplicationContext app: Context): LocationSourceApplication {
        return app as LocationSourceApplication
    }

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context) =
        AppDatabase.getDatabase(appContext)

}