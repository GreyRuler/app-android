package ru.netology.nmedia.repo.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.repo.PostRepository

class InMemoryPostRepository : PostRepository {

    private val posts get() = checkNotNull(data.value) {
        "Data value should not be null"
    }

    override val data = MutableLiveData(
        List(1000) { index ->
            Post(
                id = index + 1L,
                author = "Нетология. Университет интернет-профессий будущего",
                content = "Some random content $index",
                published = "19 апреля в 18:36",
                likeByMe = false,
                countLike = 999,
                countShare = 0
            )
        }
    )

    override fun like(postID: Long) {
        data.value = posts.map {
            if (it.id != postID) it
            else {
                it.copy(
                    likeByMe = !it.likeByMe,
                    countLike = boolLikeByMe(it.countLike, !it.likeByMe)
                )
            }
        }
    }

    private fun boolLikeByMe(countLike: Int, likeByMe: Boolean): Int {
        return if (likeByMe) countLike + 1 else countLike - 1
    }


    override fun share(postID: Long) {
        data.value = posts.map {
            if (it.id != postID) it
            else it.copy(countShare = it.countShare + 1)
        }
    }
}