package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeByMe: Boolean = false,
    var countLike: Int,
    var countShare: Int
)