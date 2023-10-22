package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.datasource.remote.DataDataSource
import it.giovanni.hub.data.repository.DataRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideDataDataSource(repository: DataRepository): DataDataSource {
        return repository
    }
}