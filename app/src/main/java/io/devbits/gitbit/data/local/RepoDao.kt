package io.devbits.gitbit.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.devbits.gitbit.data.Repo

@Dao
interface RepoDao {

    @Insert
    suspend fun insertRepo(repo: Repo)

    @Insert
    suspend fun insertRepos(vararg repo: Repo)

    @Query("SELECT * FROM Repo")
    fun getGithubRepos(): LiveData<List<Repo>>

}