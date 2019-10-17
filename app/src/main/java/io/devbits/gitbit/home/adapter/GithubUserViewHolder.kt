package io.devbits.gitbit.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.devbits.gitbit.R
import io.devbits.gitbit.data.User
import io.devbits.gitbit.util.context
import kotlinx.android.synthetic.main.row_user.view.*

class GithubUserViewHolder private constructor(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    fun bind(user: User) {
        itemView.username_text_view.text = user.username
        itemView.repos_text_view.text = context.getString(R.string.user_repositories, user.repoCount)
    }

    companion object {
        fun create(parent: ViewGroup): GithubUserViewHolder {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.row_user, parent, false)
            return GithubUserViewHolder(view)
        }
    }
}