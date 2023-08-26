package io.devbits.gitbit.data.repository

import androidx.lifecycle.asFlow
import io.devbits.gitbit.data.User
import io.devbits.gitbit.data.local.GithubUserDao
import io.devbits.gitbit.data.remote.GithubApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DefaultUserRepository(
    private val apiService: GithubApiService,
    private val usersDao: GithubUserDao,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default),
) : UserRepository {

    /**
     * Return a stream of all the Users that were saved in the local database.
     */
    override fun getUsers(): Flow<List<User>> {
        // TODO: Return a Flow directly from Room
        return usersDao.getUsers().asFlow()
    }

    /**
     * Get an instance of an observable User that matches the specified username.
     */
    override fun getUser(username: String): Flow<User> {
        // TODO: Implement a sync mechanism
        scope.launch(Dispatchers.IO) {
            if (usersDao.rows(username) == 0) {
                fetchAndSaveUser(username)
            }
        }
        // TODO: Return a Flow directly from Room
        return usersDao.getUser(username).asFlow()
    }

    /**
     * Fetch a user from the [GithubApiService] and save the result into
     * [io.devbits.gitbit.data.local.GithubRepoDatabase] for a user with the specified username.
     */
    override suspend fun fetchAndSaveUser(username: String) {
        val apiResponse = apiService.getGithubUser(username)
        if (apiResponse.isSuccessful) {
            val body = apiResponse.body()
            if (body != null) {
                val user = User(body.login, body.publicRepos, body.avatarUrl)
                usersDao.insertUser(user)
            }
        }
    }
}
