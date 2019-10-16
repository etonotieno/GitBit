package io.devbits.gitbit

import androidx.lifecycle.*
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.domain.GithubRepository

class MainViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _usernameLiveData = MutableLiveData<String>().apply { value = "etonotieno" }
    val usernameLiveData: LiveData<String>
        get() = _usernameLiveData

    val githubReposLiveData: LiveData<Result<List<Repo>>> =
        usernameLiveData.switchMap { username ->
            liveData {
                emit(repository.getGithubRepos(username))
            }
        }

    fun setUserName(username: String) {
        if (_usernameLiveData.value != username) {
            _usernameLiveData.value = username
        }
    }

}
