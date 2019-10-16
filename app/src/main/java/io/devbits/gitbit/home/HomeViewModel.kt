package io.devbits.gitbit.home

import android.util.Log
import androidx.lifecycle.*
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.domain.GithubRepository

class HomeViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
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

    fun setUserName(username: String) {
        if (_username.value != username) {
            _username.value = username
        }
    }

}
