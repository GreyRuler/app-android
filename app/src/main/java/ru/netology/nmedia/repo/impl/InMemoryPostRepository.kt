package ru.netology.nmedia.repo.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.repo.PostRepository

class InMemoryPostRepository : PostRepository {

    override val data = MutableLiveData(
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология. Когда-то Нетология начиналась с интенссивов",
            published = "21 мая в 18:36",
            likeByMe = false,
            countLike = 999,
            countShare = 0
        )
    )

    override fun like() {
        val currentPost = checkNotNull(data.value) {

        }
        val post = currentPost.copy(
            likeByMe = !currentPost.likeByMe
        )
        if (post.likeByMe) post.countLike += 1 else post.countLike -= 1
        data.value = post
    }

    override fun share() {
        val currentPost = checkNotNull(data.value) {

        }
        val post = currentPost.copy(
            countShare = currentPost.countShare + 1
        )
        data.value = post
    }
}