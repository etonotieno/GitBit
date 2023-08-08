package io.devbits.gitbit.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.devbits.gitbit.data.Repo
import io.devbits.gitbit.databinding.RowRepoLayoutBinding

class GithubRepoViewHolder private constructor(
    private val binding: RowRepoLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(repo: Repo) {
        binding.repositoryNameTextView.text = repo.name
        binding.descriptionTextView.text = repo.description
        binding.starCountTextView.text = repo.stars.toString()
    }

    companion object {
        fun create(parent: ViewGroup): GithubRepoViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowRepoLayoutBinding.inflate(inflater, parent, false)
            return GithubRepoViewHolder(binding)
        }
    }
}