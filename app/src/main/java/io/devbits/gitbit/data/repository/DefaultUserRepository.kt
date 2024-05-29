package io.devbits.gitbit.data.repository

import android.util.Log
import io.devbits.gitbit.data.User
import io.devbits.gitbit.data.local.GithubUserDao
import io.devbits.gitbit.data.remote.GithubApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import java.io.IOException

private const val TAG = "DefaultUserRepository"

class DefaultUserRepository(
    private val apiService: GithubApiService,
    private val usersDao: GithubUserDao,
) : UserRepository {

    /**
     * Return a stream of all the Users that were saved in the local database.
     */
    override fun getUsers(): Flow<List<User>> = usersDao.getUsers()

    /**
     * Get an instance of an observable User that matches the specified username.
     */
    override fun getUser(username: String): Flow<User> = usersDao.getUser(username)

    /**
     * Fetch a user from the [GithubApiService] and save the result into
     * [io.devbits.gitbit.data.local.GithubRepoDatabase] for a user with the specified username.
     */
    override suspend fun fetchAndSaveUser(username: String) {
        try {
            val apiResponse = apiService.getGithubUser(username)
            if (apiResponse.isSuccessful) {
                val body = apiResponse.body()
                if (body != null) {
                    val user = User(body.login, body.publicRepos, body.avatarUrl)
                    usersDao.insertUser(user)
                }
            }
        } catch (e: IOException) {
            Log.e(TAG, "IOException occurred. Issue with the network connection or output")
        } catch (e: HttpException) {
            Log.e(TAG, "Unexpected, non-2xx HTTP response")
        } catch (t: Throwable) {
            Log.e(TAG, "Error fetching and saving user: $username")
        }
    }
}
