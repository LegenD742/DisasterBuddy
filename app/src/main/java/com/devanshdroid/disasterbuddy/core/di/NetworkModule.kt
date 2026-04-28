package com.devanshdroid.disasterbuddy.core.di


import com.devanshdroid.disasterbuddy.core.network.AuthInterceptor
import com.devanshdroid.disasterbuddy.core.network.RetrofitClient
import com.devanshdroid.disasterbuddy.core.utils.Constants
import com.devanshdroid.disasterbuddy.data.remote.api.AlertApiService
import com.devanshdroid.disasterbuddy.data.remote.api.AuthApiService
import com.devanshdroid.disasterbuddy.data.remote.api.HelpRequestApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient =
        RetrofitClient.buildOkHttpClient(authInterceptor)

    @Provides
    @Singleton
    fun provideAuthApiService(okHttpClient: OkHttpClient): AuthApiService =
        RetrofitClient.createService(
            AuthApiService::class.java,
            Constants.AUTH_BASE_URL,
            okHttpClient
        )

    @Provides
    @Singleton
    fun provideAlertApiService(okHttpClient: OkHttpClient): AlertApiService =
        RetrofitClient.createService(
            AlertApiService::class.java,
            Constants.ALERT_BASE_URL,
            okHttpClient
        )

    @Provides
    @Singleton
    fun provideHelpRequestApiService(okHttpClient: OkHttpClient): HelpRequestApiService =
        RetrofitClient.createService(
            HelpRequestApiService::class.java,
            Constants.HELP_REQUEST_BASE_URL,
            okHttpClient
        )
}