package io.devbits.gitbit.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.devbits.gitbit.GitBitApp
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.data.User
import io.devbits.gitbit.data.asResult
import io.devbits.gitbit.data.repository.RepoRepository
import io.devbits.gitbit.data.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * This ViewModel class is used to fetch data for the HomeActivity screen
 */
class HomeViewModel(
    private val userRepository: UserRepository,
    private val repoRepository: RepoRepository,
    private val state: SavedStateHandle,
) : ViewModel() {

    /**
     * Return a MutableFlow from the SavedStateHandle that survives process death.
     */
    val username: StateFlow<String> = state.getStateFlow(USERNAME_KEY, "")

    /**
     * Return a Flow of a User that matches the username retrieved from the username Flow
     */
    /**
     * Get Github repositories from the Repository.
     *
     * The githubUser Flow is used to retrieve the username from the saved User.
     * Fetching the User from Github returns a formatted username that we can use to fetch Github
     * repositories for that User. This currently leads to a NullPointerException
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val reposUiState: StateFlow<RepoUiState> = username
        .filter { it.isNotBlank() }
        .flatMapLatest(repoRepository::getRepos)
        .asResult()
        .map {
            when (it) {
                is Result.Error -> RepoUiState.Error
                Result.Loading -> RepoUiState.Loading
                is Result.Success -> {
                    if (it.data.isEmpty()) RepoUiState.Empty else RepoUiState.Success(it.data)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RepoUiState.Initial,
        )

    /**
     * Return a Flow of users that were searched by the user.
     */
    val usersUiState: StateFlow<UserUiState> = userRepository.getUsers()
        .map(List<User>::reversed)
        .asResult()
        .map {
            when (it) {
                is Result.Error -> UserUiState.Error
                Result.Loading -> UserUiState.Loading
                is Result.Success -> {
                    if (it.data.isEmpty()) UserUiState.Empty else UserUiState.Success(it.data)
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserUiState.Initial,
        )

    /**
     * Set the username to the SavedStateHandle
     */
    private fun setUserName(username: String) {
        if (this.username.value != username) {
            state[USERNAME_KEY] = username
        }
    }

    private fun fetchAndSaveUserData(username: String) {
        if (username.isBlank()) return
        viewModelScope.launch {
            userRepository.fetchAndSaveUser(username)
            repoRepository.fetchAndSaveRepos(username)
        }
    }

    fun onEvent(events: HomeUiEvents) {
        when (events) {
            is HomeUiEvents.SetUserName -> {
                setUserName(events.username)
                fetchAndSaveUserData(events.username)
            }

            is HomeUiEvents.UserClick -> {
                setUserName(events.username)
            }
        }
    }

    companion object {
        const val USERNAME_KEY = "io.devbits:GITHUB_USERNAME"

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as GitBitApp
                val savedStateHandle = createSavedStateHandle()

                HomeViewModel(
                    userRepository = app.userRepository,
                    repoRepository = app.repoRepoRepository,
                    state = savedStateHandle,
                )
            }
        }
    }

}

sealed interface RepoUiState {
    data object Initial : RepoUiState
    data object Loading : RepoUiState
    data object Empty : RepoUiState

    data class Success(val repos: List<Repo>) : RepoUiState

    data object Error : RepoUiState
}

sealed interface UserUiState {
    data object Initial : UserUiState
    data object Loading : UserUiState
    data object Empty : UserUiState

    data class Success(val users: List<User>) : UserUiState

    data object Error : UserUiState
}

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val users: List<User>, val repos: List<Repo>) : HomeUiState

    data class Error(val message: String? = null, val throwable: Throwable? = null) : HomeUiState
}
