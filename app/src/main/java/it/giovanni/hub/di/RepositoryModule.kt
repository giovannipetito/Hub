package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.data.repositoryimpl.remote.AuthRepositoryImpl
import it.giovanni.hub.data.repositoryimpl.remote.ComfyRepositoryImpl
import it.giovanni.hub.data.repositoryimpl.remote.UsersRepositoryImpl
import it.giovanni.hub.domain.repositoryint.remote.AuthRepository
import it.giovanni.hub.domain.repositoryint.remote.ComfyRepository
import it.giovanni.hub.domain.repositoryint.remote.UsersRepository
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