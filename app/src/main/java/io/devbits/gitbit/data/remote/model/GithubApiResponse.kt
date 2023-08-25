package io.devbits.gitbit.data.remote.model

import com.google.gson.annotations.SerializedName

data class GithubApiResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("description") val description: String?,
    @SerializedName("updated_at") val dateUpdated: String,
    @SerializedName("stargazers_count") val stars: Int,
) {
    data class Owner(
        @SerializedName("login") val username: String,
        @SerializedName("avatar_url") val avatarUrl: String,
    )
}
