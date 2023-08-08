package io.devbits.gitbit.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import io.devbits.gitbit.R
import io.devbits.gitbit.data.User
import io.devbits.gitbit.databinding.RowUserBinding
import io.devbits.gitbit.util.context

typealias OnUserClick = (User) -> Unit

class GithubUserViewHolder private constructor(
    private val binding: RowUserBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: User, onUserClick: OnUserClick) {
        binding.usernameTextView.text = user.username
        binding.reposTextView.text =
            context?.getString(R.string.user_repositories, user.repoCount)
        binding.userAvatarImageView.load(user.avatarUrl) {
            placeholder(R.drawable.ic_account_circle_black_24dp)
            error(R.drawable.ic_account_circle_black_24dp)
            transformations(CircleCropTransformation())
        }
        binding.root.setOnClickListener { onUserClick.invoke(user) }
    }

    companion object {
        fun create(parent: ViewGroup): GithubUserViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowUserBinding.inflate(inflater, parent, false)
            return GithubUserViewHolder(binding)
        }
    }
}