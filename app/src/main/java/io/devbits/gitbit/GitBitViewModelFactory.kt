package io.devbits.gitbit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.devbits.gitbit.domain.GithubRepository
import io.devbits.gitbit.home.MainViewModel

@Suppress("UNCHECKED_CAST")
class GitBitViewModelFactory(
    private val repository: GithubRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}