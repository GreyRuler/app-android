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

    override fun like(): String {
        val currentPost = checkNotNull(data.value) {

        }
        val post = currentPost.copy(
            likeByMe = !currentPost.likeByMe
        )
        data.value = post
        if (post.likeByMe) post.countLike += 1 else post.countLike -= 1
        return conversionCountLike(post.countLike)
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

    override fun share(): String {
        val currentPost = checkNotNull(data.value) {

        }
        val post = currentPost.copy(
            countShare = currentPost.countShare + 1
        )
        data.value = post
        return "${post.countShare}"
    }
}