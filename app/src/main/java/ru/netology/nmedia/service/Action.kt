package ru.netology.nmedia.service

internal enum class Action(
    val key: String
) {
    Like("LIKE");

    companion object {
        const val KEY = "action"
    }
}