package io.devbits.gitbit.util

import android.view.View
import androidx.recyclerview.widget.RecyclerView

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

val RecyclerView.ViewHolder.context
    get() = itemView.context