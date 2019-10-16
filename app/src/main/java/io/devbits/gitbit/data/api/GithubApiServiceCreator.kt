package io.devbits.gitbit.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object GithubApiServiceCreator {

    fun getRetrofitClient(): GithubApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create()
    }

    private const val BASE_URL = "https://api.github.com"
}