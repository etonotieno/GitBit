package io.devbits.gitbit.data.remote.model

import com.google.gson.annotations.SerializedName
import io.devbits.gitbit.data.Repo

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

/**
 * Map a GithubApiResponse to a Repo class to be used in the UI layer.
 */
fun GithubApiResponse.mapToRepo(): Repo {
    return Repo(
        id = id,
        name = name,
        stars = stars,
        description = description.orEmpty(),
        ownerUsername = owner.username
    )
}
