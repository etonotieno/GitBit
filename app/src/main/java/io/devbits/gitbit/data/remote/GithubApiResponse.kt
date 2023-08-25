package io.devbits.gitbit.data.remote

import com.google.gson.annotations.SerializedName

data class GithubUser(
    @SerializedName("login")
    val login: String,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("avatar_url")
    val avatarUrl: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("company")
    val company: String?,
    @SerializedName("repos_url")
    val reposUrl: String?,
    @SerializedName("blog")
    val blog: String?,
)
