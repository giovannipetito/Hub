package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.datasource.remote.AuthDataSource
import it.giovanni.hub.data.datasource.remote.NetworkDataSource
import it.giovanni.hub.data.datasource.remote.UsersDataSource
import it.giovanni.hub.data.datasource.remote.impl.AuthDataSourceImpl
import it.giovanni.hub.data.datasource.remote.impl.NetworkDataSourceImpl
import it.giovanni.hub.data.datasource.remote.impl.UsersDataSourceImpl
import it.giovanni.hub.domain.usecase.PasswordValidator
import it.giovanni.hub.domain.usecase.PasswordValidatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideUsersDataSource(dataSource: UsersDataSourceImpl): UsersDataSource {
        return dataSource
    }

    @Provides
    @Singleton
    fun provideAuthDataSource(dataSource: AuthDataSourceImpl): AuthDataSource {
        return dataSource
    }

    @Provides
    @Singleton
    fun providePasswordValidator(passwordValidator: PasswordValidatorImpl): PasswordValidator {
        return passwordValidator
    }

    @Provides
    @Singleton
    fun provideNetworkDataSource(dataSource: NetworkDataSourceImpl): NetworkDataSource {
        return dataSource
    }
}