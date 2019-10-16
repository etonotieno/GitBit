package io.devbits.gitbit.home

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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

        search_button.setOnClickListener {
            val username = username_edit_text.text.toString()
            if (username.isNotBlank()) {
                viewModel.setUserName(username)
            }
        }

        viewModel.usernameLiveData.observe(this, Observer {
            username_edit_text.setText(it)
        })

    }

}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}