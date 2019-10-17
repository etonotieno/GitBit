package io.devbits.gitbit.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.devbits.gitbit.data.Repo

class GithubRepoAdapter : ListAdapter<Repo, GithubRepoViewHolder>(REPO_DIFF) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GithubRepoViewHolder {
        return GithubRepoViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GithubRepoViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val REPO_DIFF = object : DiffUtil.ItemCallback<Repo>() {
            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }

            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }
}