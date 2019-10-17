package io.devbits.gitbit.home

import android.util.Log
import androidx.lifecycle.*
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.domain.GithubRepository

class HomeViewModel(
    private val repository: GithubRepository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val _username = state.getLiveData<String>(USERNAME_KEY)
    val usernameLiveData: LiveData<String>
        get() = _username

    val githubRepos: LiveData<Result<List<Repo>>> = _username.switchMap { username ->
        liveData<Result<List<Repo>>> {
            emit(Result.Loading)
            try {
                val reposLiveData = repository.getRepos(username)
                emitSource(reposLiveData.map { Result.Success(it) })
            } catch (exception: Exception) {
                Log.e("GithubApi", "Get Github Repos Failed", exception)
                emit(Result.Error(exception, "The app encountered an unexpected error"))
            }
        }
    }

    // TODO: Use a Result wrapper to show LOADING, ERROR and SUCCESS states
    val githubUsers = repository.getGithubUsers()

    fun setUserName(username: String) {
        if (_username.value != username) {
            state[USERNAME_KEY] = username
        }
    }

    companion object {
        const val USERNAME_KEY = "io.devbits:GITHUB_USERNAME"
    }

}
