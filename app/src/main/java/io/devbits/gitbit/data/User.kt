package io.devbits.gitbit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey
    val username: String,
    val repoCount: Int
)