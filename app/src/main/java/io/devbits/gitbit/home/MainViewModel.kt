package io.devbits.gitbit.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.domain.GithubRepository

class MainViewModel(
    private val repository: GithubRepository
) : ViewModel() {

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    val githubRepos: LiveData<Result<List<Repo>>> =
        repository.getRepos(_username.value.toString())

    fun setUserName(username: String) {
        if (_username.value != username) {
            _username.value = username
        }
    }

}
