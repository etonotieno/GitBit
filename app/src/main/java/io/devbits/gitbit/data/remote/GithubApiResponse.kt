package io.devbits.gitbit.data.remote

import com.google.gson.annotations.SerializedName

data class GithubApiResponse(
    @field:SerializedName("id") val id: Int,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("owner") val owner: Owner,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("updated_at") val dateUpdated: String,
    @field:SerializedName("stargazers_count") val stars: Int
)

data class Owner(
    @field:SerializedName("login") val username: String,
    @field:SerializedName("avatar_url") val avatarUrl: String
)