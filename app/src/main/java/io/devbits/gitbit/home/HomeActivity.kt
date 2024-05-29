package io.devbits.gitbit.home

import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import io.devbits.gitbit.R
import io.devbits.gitbit.databinding.ActivityHomeBinding
import io.devbits.gitbit.home.adapter.GithubRepoAdapter
import io.devbits.gitbit.home.adapter.GithubUserAdapter
import io.devbits.gitbit.util.hide
import io.devbits.gitbit.util.show
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private val reposAdapter = GithubRepoAdapter()
    private val usersAdapter = GithubUserAdapter()

    private val viewModel: HomeViewModel by viewModels { HomeViewModel.Factory }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        binding.reposRecyclerView.adapter = reposAdapter
        binding.savedUsersRecyclerView.adapter = usersAdapter

        initSearchInputListener()

        usersAdapter.setOnUserClickListener {
            dismissKeyboard(binding.usernameEditText.windowToken)
            viewModel.onEvent(HomeUiEvents.UserClick(it.username))
        }

        lifecycleScope.launch {
            viewModel.usersUiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest {
                    when (it) {
                        UserUiState.Empty -> {}

                        UserUiState.Error -> {}

                        UserUiState.Initial -> {}

                        UserUiState.Loading -> {}

                        is UserUiState.Success -> {
                            usersAdapter.submitList(it.users) {
                                binding.savedUsersRecyclerView.smoothScrollToPosition(0)
                            }
                        }
                    }
                }
        }

        lifecycleScope.launch {
            viewModel.username
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { binding.usernameEditText.setText(it) }
        }

        lifecycleScope.launch {
            viewModel.reposUiState
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collectLatest { result ->
                    when (result) {
                        RepoUiState.Error -> {
                            binding.reposRecyclerView.hide()
                            binding.progressBar.hide()
                            binding.emptyStateTextView.show()
                            binding.emptyStateTextView.text =
                                getString(R.string.error_fetching_repos)
                        }

                        RepoUiState.Initial -> {
                            binding.reposRecyclerView.hide()
                            binding.progressBar.hide()
                            binding.emptyStateTextView.show()
                            binding.emptyStateTextView.text =
                                getString(R.string.fill_username)
                        }

                        RepoUiState.Loading -> {
                            binding.reposRecyclerView.hide()
                            binding.progressBar.show()
                            binding.emptyStateTextView.hide()
                        }

                        is RepoUiState.Success -> {
                            binding.reposRecyclerView.show()
                            binding.progressBar.hide()
                            binding.emptyStateTextView.hide()
                            reposAdapter.submitList(result.repos)
                        }

                        RepoUiState.Empty -> {
                            binding.reposRecyclerView.hide()
                            binding.progressBar.hide()
                            binding.emptyStateTextView.show()
                            binding.emptyStateTextView.text =
                                getString(R.string.no_repos_found)
                        }
                    }
                }
        }
    }

    private fun initSearchInputListener() {
        binding.usernameEditText.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        binding.usernameEditText.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val username = binding.usernameEditText.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        viewModel.onEvent(HomeUiEvents.SetUserName(username))
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService<InputMethodManager>()
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}
