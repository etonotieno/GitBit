package io.devbits.gitbit.home

sealed interface HomeUiEvents {
    data class SetUserName(val username: String) : HomeUiEvents
    data class UserClick(val username: String) : HomeUiEvents
}
