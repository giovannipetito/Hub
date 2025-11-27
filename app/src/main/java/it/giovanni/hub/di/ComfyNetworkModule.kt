package it.giovanni.hub.di

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.BuildConfig
import it.giovanni.hub.data.api.ComfyApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ComfyNetworkModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Provides
    @Singleton
    @Named("comfyBaseUrl")
    fun provideComfyOkHttpClient(
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                // .addHeader("Authorization", "Bearer ${BuildConfig.COMFY_ICU_API_KEY}")
                .addHeader("accept", "application/json")
                .addHeader("content-type", "application/json")
                .build()
            chain.proceed(newRequest)
        }
        .apply { if (BuildConfig.DEBUG) addInterceptor(loggingInterceptor) }
        .build()

    @Provides
    @Singleton
    @Named("comfyBaseUrl")
    fun provideComfyRetrofit(
        @Named("comfyBaseUrl") client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl("http://localhost/") // Use baseUrl(Config.COMFY_BASE_URL) if the URL is fixed and you don't need to read it from DataStoreRepository.
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    @Provides
    @Singleton
    @Named("comfyBaseUrl")
    fun provideComfyApiService(
        @Named("comfyBaseUrl") retrofit: Retrofit
    ): ComfyApiService = retrofit.create(ComfyApiService::class.java)
}