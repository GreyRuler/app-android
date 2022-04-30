package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология. Когда-то Нетология начиналась с интенссивов",
            published = "21 мая в 18:36",
            likeByMe = false,
            countLike = 999,
            countShare = 0
        )

        binding.render(post)
        with(binding) {
            like.setOnClickListener{
                post.likeByMe = !post.likeByMe
                like.setImageResource(getLikeIconResID(post.likeByMe))
                changeLikeCount(post.likeByMe, post)
                countLike.text = conversionCountLike(post)
            }
            share.setOnClickListener {
                post.countShare += 1
                countShare.text = "${post.countShare}"
            }
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        like.setImageResource(getLikeIconResID(post.likeByMe))
        countLike.text = conversionCountLike(post)
    }

    @DrawableRes
    private fun getLikeIconResID(liked: Boolean): Int {
       return if (liked) R.drawable.ic_liked_24_red else R.drawable.ic_like_24
    }

    private fun changeLikeCount(liked: Boolean, post: Post) {
        if (liked) post.countLike += 1 else post.countLike -= 1
    }

    private fun conversionCountLike(post: Post): String {
        return when (post.countLike) {
            in 0..999 -> "${post.countLike}"
            in 1_000..1_099 -> "1K"
            in 1_100..9_999 -> "${post.countLike / 1000}.${post.countLike / 100 % 10}K"
            in 10_000..999_999 -> "${post.countLike / 1000}K"
            in 1_000_000..1_099_999 -> "1M"
            else -> "${post.countLike / 1_000_000}.${post.countLike / 100_000 % 10}M"
        }
    }
}