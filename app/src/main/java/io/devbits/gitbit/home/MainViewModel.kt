package io.devbits.gitbit.home

import androidx.lifecycle.*
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.domain.GithubRepository

class MainViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _usernameLiveData = MutableLiveData<String>()
    val usernameLiveData: LiveData<String>
        get() = _usernameLiveData

    val githubReposLiveData: LiveData<Result<List<Repo>>> =
        _usernameLiveData.switchMap { username ->
            liveData {
                emit(repository.getGithubRepos(username))
            }
        }

    fun setUserName(username: String) {
        if (usernameLiveData.value != username) {
            _usernameLiveData.value = username
        }
    }

}
