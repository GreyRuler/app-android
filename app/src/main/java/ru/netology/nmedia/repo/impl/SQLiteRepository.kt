package ru.netology.nmedia.repo.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.repo.PostRepository

class SQLiteRepository(
    private val dao: PostDao
) : PostRepository {

    private val posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }

    override val data = MutableLiveData(dao.getAll())

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)
        data.value = if (id == 0L) {
            listOf(saved) + posts
        } else posts.map {
            if (it.id != id) it else saved
        }
    }


    override fun like(postID: Long) {
        dao.likeById(postID)
        data.value = posts.map {
            if (it.id != postID) it else it.copy(
                likedByMe = !it.likedByMe,
                likes = if (it.likedByMe) it.likes - 1 else it.likes + 1
            )
        }
    }


    override fun share(postID: Long) {
        dao.shareById(postID)
        data.value = posts.map {
            if (it.id != postID) it
            else it.copy(reposts = it.reposts + 1)
        }
    }

    override fun delete(postID: Long) {
        dao.removeById(postID)
        data.value = posts.filter { it.id != postID }
    }
}