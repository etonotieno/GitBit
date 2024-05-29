package io.devbits.gitbit

import android.app.Application
import io.devbits.gitbit.data.local.GithubRepoDatabase
import io.devbits.gitbit.data.local.GithubUserDao
import io.devbits.gitbit.data.local.RepoDao
import io.devbits.gitbit.data.remote.GithubApiService
import io.devbits.gitbit.data.remote.GithubApiServiceCreator
import io.devbits.gitbit.data.repository.DefaultRepoRepository
import io.devbits.gitbit.data.repository.DefaultUserRepository
import io.devbits.gitbit.data.repository.RepoRepository
import io.devbits.gitbit.data.repository.UserRepository

class GitBitApp : Application() {

    private lateinit var database: GithubRepoDatabase
    private lateinit var repoDao: RepoDao
    private lateinit var userDao: GithubUserDao
    private lateinit var apiService: GithubApiService

    lateinit var userRepository: UserRepository
        private set

    lateinit var repoRepoRepository: RepoRepository
        private set

    override fun onCreate() {
        super.onCreate()

        database = GithubRepoDatabase(this)
        repoDao = database.repoDao()
        userDao = database.userDao()

        apiService = GithubApiServiceCreator.getRetrofitClient(this)

        repoRepoRepository = DefaultRepoRepository(apiService = apiService, repoDao = repoDao)
        userRepository = DefaultUserRepository(apiService = apiService, usersDao = userDao)
    }
}
