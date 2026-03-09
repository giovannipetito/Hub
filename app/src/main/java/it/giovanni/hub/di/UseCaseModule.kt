package it.giovanni.hub.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.domain.repository.remote.CalendarBackupRepository
import it.giovanni.hub.domain.repository.remote.UsersRepository
import it.giovanni.hub.domain.usecase.DeleteImportedGoogleEventUseCase
import it.giovanni.hub.domain.usecase.DisableBirthdayBackupUseCase
import it.giovanni.hub.domain.usecase.EnableBirthdayBackupUseCase
import it.giovanni.hub.domain.usecase.GetCoroutinesUsersUseCase
import it.giovanni.hub.domain.usecase.GetRxJavaUsersUseCase
import it.giovanni.hub.domain.usecase.ImportGoogleCalendarEventsUseCase
import it.giovanni.hub.domain.usecase.ObserveBirthdayBackupEnabledUseCase
import it.giovanni.hub.domain.usecase.SearchRxJavaUsersUseCase
import it.giovanni.hub.domain.usecase.SearchCoroutinesUsersUseCase
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
    fun provideEnableBirthdayBackupUseCase(
        repository: CalendarBackupRepository
    ): EnableBirthdayBackupUseCase = EnableBirthdayBackupUseCase(repository)

    @Provides
    @Singleton
    fun provideDisableBirthdayBackupUseCase(
        repository: CalendarBackupRepository
    ): DisableBirthdayBackupUseCase = DisableBirthdayBackupUseCase(repository)

    @Provides
    @Singleton
    fun provideObserveBirthdayBackupEnabledUseCase(
        repository: CalendarBackupRepository
    ): ObserveBirthdayBackupEnabledUseCase = ObserveBirthdayBackupEnabledUseCase(repository)

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
}