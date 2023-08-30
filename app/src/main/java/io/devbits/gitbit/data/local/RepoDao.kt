package io.devbits.gitbit.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import io.devbits.gitbit.data.Repo
import kotlinx.coroutines.flow.Flow

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)

    @Query("SELECT * FROM Repo WHERE ownerUsername =:username ORDER BY stars DESC")
    fun getGithubRepos(username: String): Flow<List<Repo>>

    @Query("SELECT COUNT(id) FROM Repo WHERE ownerUsername =:username")
    suspend fun rows(username: String): Int?

    @Query("SELECT ownerUsername FROM Repo WHERE ownerUsername =:username")
    fun getUsers(username: String): Flow<List<String>>

    @Transaction
    suspend fun hasRepos(username: String): Boolean {
        return (rows(username) ?: 0) > 0
    }

}
