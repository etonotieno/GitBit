package io.devbits.gitbit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.devbits.gitbit.data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface GithubUserDao {

    @Query("SELECT * FROM User")
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM User WHERE username =:username")
    fun getUser(username: String): Flow<User>

    @Query("SELECT COUNT(username) FROM User WHERE username =:username")
    suspend fun rows(username: String): Int

}
