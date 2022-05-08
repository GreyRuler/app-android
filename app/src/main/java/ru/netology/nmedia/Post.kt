package ru.netology.nmedia

import kotlinx.serialization.Serializable

@Serializable
data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likeByMe: Boolean = false,
    var countLike: Int,
    var countShare: Int,
    var url: String?
)
