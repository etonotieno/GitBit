package io.devbits.gitbit.data.repository

import io.devbits.gitbit.data.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getUsers(): Flow<List<User>>

    fun getUser(username: String): Flow<User>

    suspend fun fetchAndSaveUser(username: String)
}
