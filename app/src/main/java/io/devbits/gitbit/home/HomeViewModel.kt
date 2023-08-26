package io.devbits.gitbit.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.data.User
import io.devbits.gitbit.data.repository.RepoRepository
import io.devbits.gitbit.data.repository.UserRepository

/**
 * This ViewModel class is used to fetch data for the HomeActivity screen
 */
class HomeViewModel(
    private val userRepository: UserRepository,
    private val repoRepository: RepoRepository,
    private val state: SavedStateHandle,
) : ViewModel() {

    /**
     * Return a MutableLiveData from the SavedStateHandle that survives process death.
     */
    private val _username: MutableLiveData<String> = state.getLiveData(USERNAME_KEY)
    val usernameLiveData: LiveData<String>
        get() = _username

    val githubRepos: LiveData<Result<List<Repo>>> = _username.switchMap { username ->
        liveData {
            emit(Result.Loading)
            try {
                val reposLiveData = repoRepository.getRepos(username).asLiveData()
                emitSource(reposLiveData.map { Result.Success(it) })
            } catch (exception: Exception) {
                Log.e("GithubApi", "Get Github Repos Failed", exception)
                emit(Result.Error(exception, "The app encountered an unexpected error"))
            }
        }
    }

    // TODO: Use a Result wrapper to show LOADING, ERROR and SUCCESS states
    /**
     * Return a LiveData of users that were searched by the user.
     */
    val githubUsers: LiveData<List<User>> = userRepository.getUsers().asLiveData()

    /**
     * Return a LiveData of a User that matches the username retrieved from the _username LiveData
     */
    private val githubUser: LiveData<User> = _username.switchMap { username ->
        liveData {
            try {
                emitSource(userRepository.getUser(username).asLiveData())
            } catch (e: Exception) {
                Log.e("GithubApi", "Get Github User Failed", e)
            }
        }
    }

    /**
     * Get Github repositories from the Repository.
     *
     * The githubUser LiveData is used to retrieve the username from the saved User.
     * Fetching the User from Github returns a formatted username that we can use to fetch Github
     * repositories for that User. This currently leads to a NullPointerException
     */
    val repos: LiveData<Result<List<Repo>>> = githubUser.switchMap { user ->
        liveData {
            emit(Result.Loading)
            try {
                val reposLiveData = repoRepository.getRepos(user.username).asLiveData()
                emitSource(reposLiveData.map { Result.Success(it) })
            } catch (exception: Exception) {
                Log.e("GithubApi", "Get Github Repos Failed", exception)
                emit(Result.Error(exception, "The app encountered an unexpected error"))
            }
        }
    }

    /**
     * Set the username to the SavedStateHandle
     */
    fun setUserName(username: String) {
        if (_username.value != username) {
            state[USERNAME_KEY] = username
        }
    }

    companion object {
        const val USERNAME_KEY = "io.devbits:GITHUB_USERNAME"
    }

}
