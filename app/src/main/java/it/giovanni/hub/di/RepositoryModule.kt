package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.repository.remote.AuthRepositoryImpl
import it.giovanni.hub.data.repository.remote.ComfyRepositoryImpl
import it.giovanni.hub.data.repository.remote.UsersRepositoryImpl
import it.giovanni.hub.domain.repository.remote.AuthRepository
import it.giovanni.hub.domain.repository.remote.ComfyRepository
import it.giovanni.hub.domain.repository.remote.UsersRepository
import it.giovanni.hub.domain.usecase.PasswordValidator
import it.giovanni.hub.domain.usecase.PasswordValidatorImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideUsersRepository(repository: UsersRepositoryImpl): UsersRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideAuthRepository(repository: AuthRepositoryImpl): AuthRepository {
        return repository
    }

    @Provides
    @Singleton
    fun provideComfyRepository(repository: ComfyRepositoryImpl): ComfyRepository {
        return repository
    }

    @Provides
    @Singleton
    fun providePasswordValidator(passwordValidator: PasswordValidatorImpl): PasswordValidator {
        return passwordValidator
    }
}