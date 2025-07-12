package it.giovanni.hub.di

import android.app.Application
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import it.giovanni.hub.BuildConfig
import it.giovanni.hub.data.ComfyApiService
import it.giovanni.hub.utils.Config
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton
import java.io.File
import java.util.concurrent.TimeUnit

private const val COMFY_CACHE_SIZE = 100 * 1024 * 1024 // 10 MiB

@Module
@InstallIn(SingletonComponent::class)
object ComfyNetworkModule {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val cacheInterceptor = Interceptor { chain ->
        val response: Response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
            .maxAge(30, TimeUnit.DAYS)
            .build()
        response.newBuilder()
            .header("Cache-Control", cacheControl.toString())
            .build()
    }

    @Provides
    @Singleton
    @Named("comfyBaseUrl")
    fun provideComfyCache(application: Application): Cache =
        Cache(File(application.cacheDir, "comfyCache"), COMFY_CACHE_SIZE.toLong())

    @Provides
    @Singleton
    @Named("comfyBaseUrl")
    fun provideComfyOkHttpClient(
        cache: Cache
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addNetworkInterceptor(cacheInterceptor)
        .addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer ${BuildConfig.COMFY_ICU_API_KEY}")
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
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(Config.COMFY_ICU_BASE_URL)
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