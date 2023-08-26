package io.devbits.gitbit.home

import androidx.lifecycle.SavedStateHandle
import io.devbits.gitbit.MainDispatcherRule
import io.devbits.gitbit.data.repository.TestRepoRepository
import io.devbits.gitbit.data.repository.TestUserRepository
import org.junit.Before
import org.junit.Rule

class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val userRepository = TestUserRepository()
    private val repoRepository = TestRepoRepository()

    private val savedStateHandle = SavedStateHandle()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(
            userRepository = userRepository,
            repoRepository = repoRepository,
            state = savedStateHandle,
        )
    }


}
