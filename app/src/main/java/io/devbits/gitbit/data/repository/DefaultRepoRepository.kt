package io.devbits.gitbit.data.repository

import android.util.Log
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiService
import io.devbits.gitbit.data.remote.model.GithubApiResponse
import io.devbits.gitbit.data.remote.model.mapToRepo
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "DefaultRepoRepository"

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
        try {
            val apiResponse = apiService.getRepositories(username)
            if (apiResponse != null) {
                val repos = apiResponse.map(GithubApiResponse::mapToRepo)
                repoDao.insertRepos(repos)
            }
        } catch (e: IOException) {
            Log.e(TAG, "IOException occurred. Issue with the network connection or output")
        } catch (e: HttpException) {
            Log.e(TAG, "Unexpected, non-2xx HTTP response")
        } catch (t: Throwable) {
            Log.e(TAG, "Error fetching and saving repos: $username")
        }
    }
}
