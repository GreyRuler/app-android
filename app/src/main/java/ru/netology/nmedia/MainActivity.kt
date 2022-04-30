package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.DrawableRes
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel = PostViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this) { post ->
            binding.render(post)
        }

        with(binding) {
            like.setOnClickListener {
                viewModel.onLikedClicked()
            }
            share.setOnClickListener {
                viewModel.onShareClicked()
            }
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        like.setImageResource(getLikeIconResID(post.likeByMe))
        countLike.text = conversionCountLike(post.countLike)
        countShare.text = post.countShare.toString()
    }

    @DrawableRes
    fun getLikeIconResID(liked: Boolean): Int {
        return if (liked) R.drawable.ic_liked_24_red else R.drawable.ic_like_24
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
}