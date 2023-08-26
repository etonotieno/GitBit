package io.devbits.gitbit

import androidx.core.os.bundleOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import io.devbits.gitbit.data.repository.RepoRepository
import io.devbits.gitbit.data.repository.UserRepository
import io.devbits.gitbit.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class GitBitViewModelFactory(
    private val userRepository: UserRepository,
    private val repoRepository: RepoRepository,
    owner: SavedStateRegistryOwner,
) : AbstractSavedStateViewModelFactory(owner, bundleOf()) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle,
    ): T {
        return HomeViewModel(
            userRepository = userRepository,
            repoRepository = repoRepository,
            state = handle
        ) as T
    }

}
