package ru.netology.nmedia.repo.impl

import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.repo.PostRepository

class InMemoryPostRepository : PostRepository {

    private val posts get() = checkNotNull(data.value) {
        "Data value should not be null"
    }

    override val data = MutableLiveData(
        List(10) { index ->
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

    override fun like(postID: Long): String {
        data.value = posts.map {
            if (it.id != postID) it
            else {
                it.copy(
                    likeByMe = !it.likeByMe,
                    countLike = it.countLike + boolLikeByMe(it)
                )
            }
        }
        val posts = checkNotNull(data.value)
        val post = posts.find { it.id == postID }
        return conversionCountLike(post!!.countLike)
    }

    private fun boolLikeByMe(post: Post): Int {
        return if (post.likeByMe) 1 else -1
    }


    private fun conversionCountLike(countLike: Int): String {
        return when (countLike) {
            in 0..999 -> "$countLike"
            in 1_000..1_099 -> "1K"
            in 1_100..9_999 -> "${countLike / 1000}.${countLike / 100 % 10}K"
            in 10_000..999_999 -> "${countLike / 1000}K"
            in 1_000_000..1_099_999 -> "1M"
            else -> "${countLike / 1_000_000}.${countLike / 100_000 % 10}M"
        }
    }

    override fun share(postID: Long): String {
        data.value = posts.map {
            if (it.id != postID) it
            else it.copy(countShare = it.countShare)
        }
        return "${data.value!!.find { it.id == postID }!!.countShare}"
    }
}