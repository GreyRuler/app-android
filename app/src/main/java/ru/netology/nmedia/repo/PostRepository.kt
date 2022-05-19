package ru.netology.nmedia.repo

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    val data: LiveData<List<Post>>

    fun like(postID: Long)
    fun share(postID: Long)
    fun delete(postID: Long)
    fun save(post: Post)

    companion object {
        const val NEW_POST_ID = 0L
    }
}