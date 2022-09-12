package io.devbits.gitbit.domain

import androidx.lifecycle.LiveData
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.User
import io.devbits.gitbit.data.local.GithubUserDao
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiResponse
import io.devbits.gitbit.data.remote.GithubApiService

/**
 * This class is used to fetch data from a remote or local data source. The local data source (Room)
 * is used as the source of truth.
 */
class GithubRepository(
    private val apiService: GithubApiService,
    private val repoDao: RepoDao,
    private val usersDao: GithubUserDao
) {

    /**
     * If there's no data in the Room database, fetch and save github repositories into the DB.
     * Since RepoDao returns LiveData, once the insert is made, the LiveData is updated.
     */
    suspend fun getRepos(username: String): LiveData<List<Repo>> {
        if (repoDao.rows(username) == 0) {
            fetchAndSaveRepos(username)
        }
        return repoDao.getGithubRepos(username)
    }

    /**
     * Get a single LiveData of a User that matches the specified username.
     */
    suspend fun getGithubUser(username: String): LiveData<User> {
        if (usersDao.rows(username) == 0) {
            fetchAndSaveUser(username)
        }
        return usersDao.getUser(username)
    }

    /**
     * Return a LiveData of all the Users that were fetched.
     */
    fun getGithubUsers(): LiveData<List<User>> {
        return usersDao.getUsers()
    }

    /**
     * Fetch repos from the GithubApiService and save the result into the Room database
     * for a user with the specified username.
     */
    private suspend fun fetchAndSaveRepos(username: String) {
        val apiResponse = apiService.getRepositories(username)
        val repos = apiResponse.map(GithubApiResponse::mapToRepo)
        repoDao.insertRepos(repos)
    }

    /**
     * Fetch a user from the GithubApiService and save the result into the Room database
     * for a user with the specified username.
     */
    private suspend fun fetchAndSaveUser(username: String) {
        val apiResponse = apiService.getGithubUser(username)
        val user = User(apiResponse.login, apiResponse.publicRepos, apiResponse.avatarUrl)
        usersDao.insertUser(user)
    }

}

/**
 * Map a GithubApiResponse to a Repo class to be used in the UI layer.
 */
fun GithubApiResponse.mapToRepo(): Repo {
    return Repo(
        name,
        id,
        stars,
        description ?: "",
        owner.username
    )
}