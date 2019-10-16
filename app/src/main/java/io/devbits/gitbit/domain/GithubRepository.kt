package io.devbits.gitbit.domain

import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.data.api.GithubApiResponse
import io.devbits.gitbit.data.api.GithubApiService

class GithubRepository(private val apiService: GithubApiService) {

    suspend fun getGithubRepos(username: String): Result<List<Repo>> {
        return try {
            Result.Loading

            val apiResponse = apiService.getRepositories(username)

            val githubRepos = apiResponse.map { it.mapToRepo() }
            Result.Success(githubRepos)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}

fun GithubApiResponse.mapToRepo(): Repo {
    return Repo(
        name,
        id,
        stars,
        description ?: ""
    )
}