package io.devbits.gitbit.data.remote

import io.devbits.gitbit.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object GithubApiServiceCreator {

    private fun createOkhttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    fun getRetrofitClient(): GithubApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkhttpClient())
            .build()
            .create()
    }

    private const val BASE_URL = "https://api.github.com"
}