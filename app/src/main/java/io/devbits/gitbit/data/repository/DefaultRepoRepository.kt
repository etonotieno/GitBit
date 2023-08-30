package io.devbits.gitbit.data.repository

import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiService
import io.devbits.gitbit.data.remote.model.GithubApiResponse
import io.devbits.gitbit.data.remote.model.mapToRepo
import kotlinx.coroutines.flow.Flow

class DefaultRepoRepository(
    private val apiService: GithubApiService,
    private val repoDao: RepoDao,
) : RepoRepository {

    /**
     * If there's no data in the Room database, fetch and save github repositories into the DB.
     * Since RepoDao returns Flow, once the insert is made, the Flow is updated.
     */
    override fun getRepos(username: String): Flow<List<Repo>> = repoDao.getGithubRepos(username)

    /**
     * Fetch repos from the [GithubApiService] and save the result into
     * [io.devbits.gitbit.data.local.GithubRepoDatabase] for a user with the specified username.
     */
    override suspend fun fetchAndSaveRepos(username: String) {
        val apiResponse = apiService.getRepositories(username)
        if (apiResponse != null) {
            val repos = apiResponse.map(GithubApiResponse::mapToRepo)
            repoDao.insertRepos(repos)
        }
    }
}
