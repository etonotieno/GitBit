package io.devbits.gitbit

import androidx.core.os.bundleOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import io.devbits.gitbit.domain.GithubRepository
import io.devbits.gitbit.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class GitBitViewModelFactory(
    private val repository: GithubRepository,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, bundleOf()) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return HomeViewModel(repository, handle) as T
    }

}