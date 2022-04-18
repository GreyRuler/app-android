package ru.netology.nmedia.repo

import androidx.lifecycle.LiveData
import ru.netology.nmedia.Post

interface PostRepository {

    val data: LiveData<Post>

    fun like (): String

    fun share (): String
}