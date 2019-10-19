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
import androidx.lifecycle.observe
import io.devbits.gitbit.GitBitViewModelFactory
import io.devbits.gitbit.R
import io.devbits.gitbit.data.Result
import io.devbits.gitbit.data.local.GithubRepoDatabase
import io.devbits.gitbit.data.remote.GithubApiServiceCreator
import io.devbits.gitbit.domain.GithubRepository
import io.devbits.gitbit.home.adapter.GithubRepoAdapter
import io.devbits.gitbit.home.adapter.GithubUserAdapter
import io.devbits.gitbit.util.hide
import io.devbits.gitbit.util.show
import kotlinx.android.synthetic.main.activity_home.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repos_recycler_view.adapter = reposAdapter
        saved_users_recycler_view.adapter = usersAdapter

        initSearchInputListener()

        usersAdapter.setOnUserClickListener {
            dismissKeyboard(username_edit_text.windowToken)
            viewModel.setUserName(it.username)
        }

        viewModel.usernameLiveData.observe(this) {
            username_edit_text.setText(it)
        }

        viewModel.githubUsers.observe(this) { users ->
            usersAdapter.submitList(users.reversed()) {
                saved_users_recycler_view.smoothScrollToPosition(0)
            }
        }

        viewModel.githubRepos.observe(this) { result ->
            when (result) {
                is Result.Success -> {
                    if (result.data.isNullOrEmpty()) {
                        repos_recycler_view.hide()
                        progress_bar.hide()
                        empty_state_text_view.show()
                        empty_state_text_view.text = getString(R.string.no_repos_found)
                        return@observe
                    }
                    repos_recycler_view.show()
                    progress_bar.hide()
                    empty_state_text_view.hide()
                    reposAdapter.submitList(result.data)
                }
                is Result.Error -> {
                    repos_recycler_view.hide()
                    progress_bar.hide()
                    empty_state_text_view.show()
                    empty_state_text_view.text = getString(R.string.error_fetching_repos)
                }
                Result.Loading -> {
                    repos_recycler_view.hide()
                    progress_bar.show()
                    empty_state_text_view.hide()
                }
            }
        }

    }

    private fun initSearchInputListener() {
        username_edit_text.setOnEditorActionListener { view: View, actionId: Int, _: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(view)
                true
            } else {
                false
            }
        }
        username_edit_text.setOnKeyListener { view: View, keyCode: Int, event: KeyEvent ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                doSearch(view)
                true
            } else {
                false
            }
        }
    }

    private fun doSearch(v: View) {
        val username = username_edit_text.text.toString()
        // Dismiss keyboard
        dismissKeyboard(v.windowToken)
        viewModel.setUserName(username)
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = getSystemService<InputMethodManager>()
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }
}