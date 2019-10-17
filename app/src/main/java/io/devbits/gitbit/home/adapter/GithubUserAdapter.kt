package io.devbits.gitbit.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.devbits.gitbit.data.User

class GithubUserAdapter : ListAdapter<User, GithubUserViewHolder>(GITHUB_USER_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubUserViewHolder {
        return GithubUserViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GithubUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val GITHUB_USER_DIFF = object : DiffUtil.ItemCallback<User>() {
            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem.username == newItem.username
            }

        }
    }
}