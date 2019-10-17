package io.devbits.gitbit.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.devbits.gitbit.R
import io.devbits.gitbit.data.Repo
import kotlinx.android.synthetic.main.row_repo_layout.view.*

class GithubRepoViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(repo: Repo) {
        itemView.repository_name_text_view.text = repo.name
        itemView.description_text_view.text = repo.description
        itemView.star_count_text_view.text = repo.stars.toString()
    }

    companion object {
        fun create(parent: ViewGroup): GithubRepoViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_repo_layout, parent, false)
            return GithubRepoViewHolder(view)
        }
    }
}