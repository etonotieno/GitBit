package io.devbits.gitbit.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.devbits.gitbit.data.User

@Dao
interface GithubUserDao {

    @Query("SELECT * FROM User")
    fun getUsers(): LiveData<List<User>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

}