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
                countLike.text = viewModel.onLikedClicked()
                like.setImageResource(getLikeIconResID(viewModel.data.value!!.likeByMe))
            }
            share.setOnClickListener {
                countShare.text = viewModel.onShareClicked()
            }
        }
    }

    private fun ActivityMainBinding.render(post: Post) {
        author.text = post.author
        published.text = post.published
        content.text = post.content
        like.setImageResource(getLikeIconResID(post.likeByMe))
    }

    @DrawableRes
    fun getLikeIconResID(liked: Boolean): Int {
        return if (liked) R.drawable.ic_liked_24_red else R.drawable.ic_like_24
    }
}