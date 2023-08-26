package io.devbits.gitbit.data.repository

import io.devbits.gitbit.data.User
import kotlinx.coroutines.flow.Flow

class TestUserRepository: UserRepository {
    override fun getUsers(): Flow<List<User>> {
        TODO("Not yet implemented")
    }

    override fun getUser(username: String): Flow<User> {
        TODO("Not yet implemented")
    }

    override suspend fun fetchAndSaveUser(username: String) {
        TODO("Not yet implemented")
    }
}
