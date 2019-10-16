package io.devbits.gitbit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.devbits.gitbit.data.Repo

@Database(entities = [Repo::class], version = 1, exportSchema = false)
abstract class GithubRepoDatabase : RoomDatabase() {

    abstract fun reoDao(): RepoDao

    companion object {
        @Volatile
        private var instance: GithubRepoDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            GithubRepoDatabase::class.java,
            "github.db"
        ).fallbackToDestructiveMigration().build()

    }
}