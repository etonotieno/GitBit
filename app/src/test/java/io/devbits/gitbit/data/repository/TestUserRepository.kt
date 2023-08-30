package io.devbits.gitbit.data.repository

import io.devbits.gitbit.data.User
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emptyFlow

class TestUserRepository : UserRepository {

    private val usersFlow: MutableSharedFlow<List<User>> =
        MutableSharedFlow(replay = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)

    override fun getUsers(): Flow<List<User>> {
        return usersFlow
    }

    override fun getUser(username: String): Flow<User> {
        return emptyFlow()
    }

    override suspend fun fetchAndSaveUser(username: String) {
        TODO("Not yet implemented")
    }
}
