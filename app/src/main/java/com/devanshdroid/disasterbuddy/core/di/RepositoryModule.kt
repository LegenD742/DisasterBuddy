package com.devanshdroid.disasterbuddy.core.di


import com.devanshdroid.disasterbuddy.data.remote.api.AlertApiService
import com.devanshdroid.disasterbuddy.data.remote.api.AuthApiService
import com.devanshdroid.disasterbuddy.data.remote.api.HelpRequestApiService
import com.devanshdroid.disasterbuddy.data.repository.AlertRepository
import com.devanshdroid.disasterbuddy.data.repository.AuthRepository
import com.devanshdroid.disasterbuddy.data.repository.HelpRequestRepository
import com.devanshdroid.disasterbuddy.core.utils.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        authApiService: AuthApiService,
        preferenceManager: PreferenceManager
    ): AuthRepository = AuthRepository(authApiService, preferenceManager)

    @Provides
    @Singleton
    fun provideAlertRepository(
        alertApiService: AlertApiService
    ): AlertRepository = AlertRepository(alertApiService)

    @Provides
    @Singleton
    fun provideHelpRequestRepository(
        helpRequestApiService: HelpRequestApiService,
        preferenceManager: PreferenceManager
    ): HelpRequestRepository = HelpRequestRepository(helpRequestApiService, preferenceManager)
}