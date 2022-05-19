package ru.netology.nmedia

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Post (
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean = false,
    var likes: Int,
    var reposts: Int,
    var url: String?
) : Parcelable
