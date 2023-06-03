package it.giovanni.hub

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.datasource.UsersDataSource
import it.giovanni.hub.repository.UsersRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideUsersDataSource(repository: UsersRepository): UsersDataSource {
        return repository
    }
}