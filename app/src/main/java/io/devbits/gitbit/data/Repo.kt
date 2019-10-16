package io.devbits.gitbit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repo(
    val name: String,
    @PrimaryKey
    val id: Int,
    val stars: Int,
    val description: String,
    val ownerUsername: String
)