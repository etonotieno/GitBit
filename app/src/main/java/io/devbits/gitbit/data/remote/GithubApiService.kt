package io.devbits.gitbit.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface GithubApiService {

    @GET("/users/{login}/repos")
    suspend fun getRepositories(
        @Path("login") username: String
    ): List<GithubApiResponse>

    @GET("users/{login}")
    suspend fun getGithubUser(
        @Path("login") username: String
    ): GithubUser

}