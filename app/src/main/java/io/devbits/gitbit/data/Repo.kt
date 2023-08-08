package io.devbits.gitbit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Repo(
    @PrimaryKey
    val id: Int,
    val name: String,
    val stars: Int,
    val description: String,
    val ownerUsername: String
)