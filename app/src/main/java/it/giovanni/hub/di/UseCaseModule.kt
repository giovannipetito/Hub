package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.domain.repositoryint.remote.UsersRepository
import it.giovanni.hub.domain.usecase.GetCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.GetRxJavaUsersUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetCoroutinesUsersUseCase(
        repository: UsersRepository
    ): GetCoroutinesUsersUseCase = GetCoroutinesUsersUseCase(repository)

    @Provides
    @Singleton
    fun provideGetRxJavaUsersUseCase(
        repository: UsersRepository
    ): GetRxJavaUsersUseCase = GetRxJavaUsersUseCase(repository)
}