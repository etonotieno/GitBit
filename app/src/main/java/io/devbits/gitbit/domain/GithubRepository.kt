package io.devbits.gitbit.domain

import androidx.lifecycle.LiveData
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiResponse
import io.devbits.gitbit.data.remote.GithubApiService

class GithubRepository(
    private val apiService: GithubApiService,
    private val repoDao: RepoDao
) {

    suspend fun getRepos(username: String): LiveData<List<Repo>> {
        if (repoDao.rows(username) == 0) {
            fetchAndSaveRepos(username)
        }
        return repoDao.getGithubRepos(username)
    }

    private suspend fun fetchAndSaveRepos(username: String) {
        val apiResponse = apiService.getRepositories(username)
        val repos = apiResponse.map { it.mapToRepo() }
        repoDao.insertRepos(repos)
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