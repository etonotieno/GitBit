package io.devbits.gitbit.data.repository

import androidx.lifecycle.asFlow
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiService
import io.devbits.gitbit.data.remote.model.GithubApiResponse
import io.devbits.gitbit.data.remote.model.mapToRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DefaultRepoRepository(
    private val apiService: GithubApiService,
    private val repoDao: RepoDao,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : RepoRepository {

    /**
     * If there's no data in the Room database, fetch and save github repositories into the DB.
     * Since RepoDao returns LiveData, once the insert is made, the LiveData is updated.
     */
    override fun getRepos(username: String): Flow<List<Repo>> {
        // TODO: Implement a sync mechanism
        scope.launch(Dispatchers.IO) {
            if (!repoDao.hasRepos(username)) {
                fetchAndSaveRepos(username)
            }
        }
        // TODO: Return a Flow directly from Room
        return repoDao.getGithubRepos(username).asFlow()
    }


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
