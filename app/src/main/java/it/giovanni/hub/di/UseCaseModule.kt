package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.domain.repository.remote.CalendarBackupRepository
import it.giovanni.hub.domain.repository.remote.UsersRepository
import it.giovanni.hub.domain.usecase.DeleteImportedGoogleEventUseCase
import it.giovanni.hub.domain.usecase.DisableBackupUseCase
import it.giovanni.hub.domain.usecase.EnableBackupUseCase
import it.giovanni.hub.domain.usecase.GetCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.GetRxJavaUsersUseCase
import it.giovanni.hub.domain.usecase.ImportGoogleCalendarEventsUseCase
import it.giovanni.hub.domain.usecase.ObserveBackupEnabledUseCase
import it.giovanni.hub.domain.usecase.SearchRxJavaUsersUseCase
import it.giovanni.hub.domain.usecase.SearchCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.SetBackupEnabledUseCase
import it.giovanni.hub.domain.usecase.UpdateImportedGoogleEventUseCase
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

    @Provides
    @Singleton
    fun provideSearchCoroutinesUsersUseCase(
        repository: UsersRepository
    ): SearchCoroutinesUsersUseCase = SearchCoroutinesUsersUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchRxJavaUsersUseCase(
        repository: UsersRepository
    ): SearchRxJavaUsersUseCase = SearchRxJavaUsersUseCase(repository)

    @Provides
    @Singleton
    fun provideEnableBackupUseCase(
        repository: CalendarBackupRepository
    ): EnableBackupUseCase = EnableBackupUseCase(repository)

    @Provides
    @Singleton
    fun provideDisableBackupUseCase(
        repository: CalendarBackupRepository
    ): DisableBackupUseCase = DisableBackupUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveBackupEnabledUseCase(
        repository: CalendarBackupRepository
    ): ObserveBackupEnabledUseCase = ObserveBackupEnabledUseCase(repository)

    @Provides
    @Singleton
    fun provideImportGoogleCalendarEventsUseCase(
        repository: CalendarBackupRepository
    ): ImportGoogleCalendarEventsUseCase = ImportGoogleCalendarEventsUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteImportedGoogleEventUseCase(
        repository: CalendarBackupRepository
    ): DeleteImportedGoogleEventUseCase = DeleteImportedGoogleEventUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateImportedGoogleEventUseCase(
        repository: CalendarBackupRepository
    ): UpdateImportedGoogleEventUseCase = UpdateImportedGoogleEventUseCase(repository)

    @Provides
    @Singleton
    fun provideSetBackupEnabledUseCase(
        repository: CalendarBackupRepository
    ): SetBackupEnabledUseCase = SetBackupEnabledUseCase(repository)
}