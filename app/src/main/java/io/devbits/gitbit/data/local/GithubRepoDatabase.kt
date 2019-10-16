package io.devbits.gitbit.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import io.devbits.gitbit.data.Repo

@Database(entities = [Repo::class], version = 1, exportSchema = false)
abstract class GithubRepoDatabase : RoomDatabase() {

    abstract fun repoDao(): RepoDao

}