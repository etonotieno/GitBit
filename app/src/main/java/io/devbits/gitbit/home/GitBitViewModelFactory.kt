package io.devbits.gitbit.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.devbits.gitbit.MainViewModel
import io.devbits.gitbit.data.api.GithubApiServiceCreator
import io.devbits.gitbit.domain.GithubRepository

@Suppress("UNCHECKED_CAST")
class GitBitViewModelFactory(
    private val repository: GithubRepository = GithubRepository(GithubApiServiceCreator.getRetrofitClient())
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}