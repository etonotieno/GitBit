package io.devbits.gitbit.domain

import androidx.lifecycle.liveData
import androidx.lifecycle.map
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiResponse
import io.devbits.gitbit.data.remote.GithubApiService

class GithubRepository(
    private val apiService: GithubApiService,
    private val repoDao: RepoDao
) {

    suspend fun getGithubRepos(username: String): Result<List<Repo>> {
        return try {
            Result.Loading

            val githubRepos = fetchRepos(username)

            Result.Success(githubRepos)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    fun getRepos(username: String) = liveData<Result<List<Repo>>> {
        emit(Result.Loading)
        val disposable = emitSource(
            repoDao.getGithubRepos(username).map {
                Result.Success(it)
            }
        )
        try {
            val repos = fetchRepos(username)
            disposable.dispose()
            repoDao.insertRepos(repos)
            emitSource(repoDao.getGithubRepos(username).map {
                Result.Success(it)
            })
        } catch (exception: Exception) {
            emit(Result.Error(exception))
        }
    }

    suspend fun fetchRepos(username: String): List<Repo> {
        val apiResponse = apiService.getRepositories(username)
        val repos = apiResponse.map { it.mapToRepo() }
        repoDao.insertRepos(repos)
        return repos
    }

}

fun GithubApiResponse.mapToRepo(): Repo {
    return Repo(
        name,
        id,
        stars,
        description ?: "",
        owner.username
    )
}