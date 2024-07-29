package it.giovanni.hub.data

import it.giovanni.hub.BuildConfig
import it.giovanni.hub.data.request.NetworkRequest
import it.giovanni.hub.data.response.NetworkResponse
import it.giovanni.hub.utils.Config
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiServiceClient {

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

    // private const val cacheSize = 10 * 1024 * 1024 // 10 MiB
    // private val cacheDirectory = File(context.cacheDir, "cache")
    // private val cache = Cache(cacheDirectory, cacheSize.toLong())

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(cacheInterceptor)
        // .cache(cache)
        // .connectTimeout(30, TimeUnit.SECONDS) // connection timeout
        // .readTimeout(30, TimeUnit.SECONDS)    // socket timeout
        // .writeTimeout(30, TimeUnit.SECONDS)   // write timeout
        .addInterceptor { chain: Interceptor.Chain ->
            val newRequest = chain.request().newBuilder()
                // .addHeader("x-rapidapi-key", BuildConfig.API_KEY)
                .addHeader("x-rapidapi-host", Config.BASE_URL3)
                // .header("User-Agent", Utils.getDeviceName()")
                .addHeader("applicationId", BuildConfig.APPLICATION_ID)
                .addHeader("app_version", BuildConfig.VERSION_NAME)
                .addHeader("os_version", android.os.Build.VERSION.RELEASE)
                .build()
            chain.proceed(newRequest)
        }
        .apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(loggingInterceptor)
            }
        }
        .build()

    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Config.BASE_URL3)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()

    private fun createApiService(): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    suspend fun sendMessage(request: NetworkRequest): NetworkResponse {
        return createApiService().sendMessage(request)
    }
}