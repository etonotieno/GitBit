package io.devbits.gitbit.home

import android.os.Bundle
import android.os.IBinder
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import io.devbits.gitbit.GitBitViewModelFactory
import io.devbits.gitbit.R
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.data.local.GithubRepoDatabase
import io.devbits.gitbit.data.remote.GithubApiServiceCreator
import io.devbits.gitbit.databinding.ActivityHomeBinding
import io.devbits.gitbit.domain.GithubRepository
import io.devbits.gitbit.home.adapter.GithubRepoAdapter
import io.devbits.gitbit.home.adapter.GithubUserAdapter
import io.devbits.gitbit.util.hide
import io.devbits.gitbit.util.show

class HomeActivity : AppCompatActivity(R.layout.activity_home) {

    private val reposAdapter = GithubRepoAdapter()
    private val usersAdapter = GithubUserAdapter()

    // TODO: Use DI to inject these dependencies
    private val database by lazy { GithubRepoDatabase(this) }
    private val repoDao by lazy { database.repoDao() }
    private val userDao by lazy { database.userDao() }
    private val apiService by lazy { GithubApiServiceCreator.getRetrofitClient() }
    private val repository by lazy { GithubRepository(apiService, repoDao, userDao) }
    private val viewModel: HomeViewModel by viewModels { GitBitViewModelFactory(repository, this) }

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.reposRecyclerView.adapter = reposAdapter
        binding.savedUsersRecyclerView.adapter = usersAdapter

        initSearchInputListener()

        usersAdapter.setOnUserClickListener {
            dismissKeyboard(binding.usernameEditText.windowToken)
            viewModel.setUserName(it.username)
        }

        viewModel.usernameLiveData.observe(this) {
            binding.usernameEditText.setText(it)
        }

        viewModel.githubUsers.observe(this) { users ->
            usersAdapter.submitList(users.reversed()) {
                binding.savedUsersRecyclerView.smoothScrollToPosition(0)
            }
        }

        viewModel.repos.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.isEmpty()) {
                        binding.reposRecyclerView.hide()
                        binding.progressBar.hide()
                        binding.emptyStateTextView.show()
                        binding.emptyStateTextView.text = getString(R.string.no_repos_found)
                        return@observe
                    }
                    binding.reposRecyclerView.show()
                    binding.progressBar.hide()
                    binding.emptyStateTextView.hide()
                    reposAdapter.submitList(result.data)
                }

                is Result.Error -> {
                    binding.reposRecyclerView.hide()
                    binding.progressBar.hide()
                    binding.emptyStateTextView.show()
                    binding.emptyStateTextView.text = getString(R.string.error_fetching_repos)
                }

                Result.Loading -> {
                    binding.reposRecyclerView.hide()
                    binding.progressBar.show()
                    binding.emptyStateTextView.hide()
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
        viewModel.setUserName(username)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService<InputMethodManager>()
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}