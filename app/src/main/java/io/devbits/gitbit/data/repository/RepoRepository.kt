package io.devbits.gitbit.data.repository

import io.devbits.gitbit.data.Repo
import kotlinx.coroutines.flow.Flow

interface RepoRepository {
    fun getRepos(username: String): Flow<List<Repo>>

    suspend fun fetchAndSaveRepos(username: String)
}
