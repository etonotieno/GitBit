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
import androidx.lifecycle.Observer
import io.devbits.gitbit.GitBitViewModelFactory
import io.devbits.gitbit.R
import io.devbits.gitbit.data.Result
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity(R.layout.main_activity) {

    private val reposAdapter = GithubRepoAdapter()
    private val viewModel: MainViewModel by viewModels { GitBitViewModelFactory() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        repos_recycler_view.adapter = reposAdapter

        viewModel.githubReposLiveData.observe(this, Observer { result ->
            when (result) {
                is Result.Success -> {
                    reposAdapter.submitList(result.data)
                    empty_state_text_view.hide()
                    progress_bar.hide()
                    repos_recycler_view.show()
                    if (result.data.isEmpty()) {
                        progress_bar.hide()
                        repos_recycler_view.hide()
                        empty_state_text_view.show()
                    }
                }
                is Result.Error -> {
                    //TODO: Show Snackbar with the error message or an error state layout
                }
                Result.Loading -> {
                    repos_recycler_view.hide()
                    empty_state_text_view.hide()
                    progress_bar.show()
                }
            }
        })

        initSearchInputListener()

        viewModel.usernameLiveData.observe(this, Observer {
            username_edit_text.setText(it)
        })

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

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}