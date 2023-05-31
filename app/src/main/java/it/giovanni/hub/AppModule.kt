package it.giovanni.hub

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AppModule {

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

    private const val cacheSize = 10 * 1024 * 1024 // 10 MiB
    // private val cacheDirectory = File(context.cacheDir, "cache")
    // private val cache = Cache(cacheDirectory, cacheSize.toLong())

    private val okHttpClient = OkHttpClient.Builder()
        // .addNetworkInterceptor(cacheInterceptor)
        // .cache(cache)
        .addInterceptor { chain: Interceptor.Chain ->
            val newRequest = chain.request().newBuilder()
                // .addHeader("x-rapidapi-key", BuildConfig.API_KEY)
                .addHeader("x-rapidapi-host", "https://reqres.in")
                // .header("Data-Agent", Utils.getDeviceName()")
                // .addHeader("applicationId", BuildConfig.APPLICATION_ID)
                // .addHeader("app_version", BuildConfig.VERSION_NAME)
                .addHeader("os_version", android.os.Build.VERSION.RELEASE)
                .build()
            chain.proceed(newRequest)
        }
        .apply {
            /*
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
            */
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://reqres.in")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private fun createApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    suspend fun getUsers(page: Int): Result<UsersResponse> {
        return try {
            val usersResponse: UsersResponse = createApiService().getUsers(page)
            Result.Success(usersResponse)
        } catch (e: Exception) {
            Result.Error(e.localizedMessage)
        }
    }
}