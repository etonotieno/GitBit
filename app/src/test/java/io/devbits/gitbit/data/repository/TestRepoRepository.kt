package io.devbits.gitbit.data.repository

import io.devbits.gitbit.data.Repo
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class TestRepoRepository : RepoRepository {
    /**
     * Hot flow that represents the stream of repositories in the Test class
     */
    private val reposFlow: MutableSharedFlow<List<Repo>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getRepos(username: String): Flow<List<Repo>> {
        return reposFlow
    }

    override suspend fun fetchAndSaveRepos(username: String) {}

    fun sendRepos(repos: List<Repo>) {
        reposFlow.tryEmit(repos)
    }
}
