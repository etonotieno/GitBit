package io.devbits.gitbit.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.devbits.gitbit.data.Repo

@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepos(repos: List<Repo>)

    @Query("SELECT * FROM Repo WHERE ownerUsername =:username ORDER BY stars DESC")
    fun getGithubRepos(username: String): LiveData<List<Repo>>

    @Query("SELECT COUNT(id) FROM Repo WHERE ownerUsername =:username")
    suspend fun rows(username: String): Int

}