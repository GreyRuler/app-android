package ru.netology.nmedia.repo.impl

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.nmedia.Post
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.db.PostDao
import ru.netology.nmedia.db.toEntity
import ru.netology.nmedia.db.toModel
import ru.netology.nmedia.repo.PostRepository

class PostRepositoryImpl(
    private val dao: PostDao
) : PostRepository {

    override val data = dao.getAll().map { entities ->
        entities.map { it.toModel() }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) dao.insert(post.toEntity())
        else dao.updateContentById(post.id, post.content, post.url)
    }

    override fun like(postID: Long) = dao.likeById(postID)

    override fun share(postID: Long) = dao.shareById(postID)

    override fun delete(postID: Long) {
        dao.removeById(postID)
    }

    companion object {
        private var instance: PostRepositoryImpl? = null
        fun getInstance(app: Application) = instance ?: PostRepositoryImpl(
            dao = AppDb.getInstance(
                context = app
            ).postDao
        ).also { instance = it }
    }
}