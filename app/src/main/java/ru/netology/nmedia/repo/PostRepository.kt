package ru.netology.nmedia.repo

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    val data: LiveData<List<Post>>

    fun like (postID: Long): String

    fun share (postID: Long): String
}