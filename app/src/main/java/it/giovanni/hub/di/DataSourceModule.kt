package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.datasource.remote.DataSource
import it.giovanni.hub.data.repository.remote.DataSourceRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideDataSource(repository: DataSourceRepository): DataSource {
        return repository
    }
}