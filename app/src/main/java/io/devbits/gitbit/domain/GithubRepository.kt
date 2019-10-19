package io.devbits.gitbit.domain

import androidx.lifecycle.LiveData
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.User
import io.devbits.gitbit.data.local.GithubUserDao
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiResponse
import io.devbits.gitbit.data.remote.GithubApiService

class GithubRepository(
    private val apiService: GithubApiService,
    private val repoDao: RepoDao,
    private val usersDao: GithubUserDao
) {

    suspend fun getRepos(username: String): LiveData<List<Repo>> {
        if (repoDao.rows(username) == 0) {
            fetchAndSaveRepos(username)
            fetchAndSaveUser(username)
        }
        return repoDao.getGithubRepos(username)
    }

    private suspend fun fetchAndSaveRepos(username: String) {
        val apiResponse = apiService.getRepositories(username)
        val repos = apiResponse.map { it.mapToRepo() }
        repoDao.insertRepos(repos)
    }

    private suspend fun fetchAndSaveUser(username: String) {
        val apiResponse = apiService.getGithubUser(username)
        val user = User(apiResponse.login, apiResponse.publicRepos, apiResponse.avatarUrl)
        usersDao.insertUser(user)
    }

    fun getGithubUsers(): LiveData<List<User>> {
        return usersDao.getUsers()
    }

    suspend fun getGithubUser(username: String): LiveData<User> {
        if (usersDao.rows(username) == 0) {
            fetchAndSaveUser(username)
        }
        return usersDao.getUser(username)
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