package ru.netology.nmedia.repo.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding

internal class PostsAdapter(
    private val onLikeClicked: (Post) -> String,
    private val onShareClicked: (Post) -> String
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, onLikeClicked, onShareClicked)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: PostBinding,
        onLikeClicked: (Post) -> String,
        onShareClicked: (Post) -> String
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        init {
            with(binding) {
                like.setOnClickListener {
                    countLike.text = onLikeClicked(post)
                }
                share.setOnClickListener {
                    countShare.text = onShareClicked(post)
                }
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                like.setImageResource(getLikeIconResID(post.likeByMe))
            }
        }

        @DrawableRes
        fun getLikeIconResID(liked: Boolean) =
            if (liked) R.drawable.ic_liked_24_red else R.drawable.ic_like_24
    }

    private object DiffCallback : DiffUtil.ItemCallback<Post>() {

        override fun areItemsTheSame(oldItem: Post, newItem: Post) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Post, newItem: Post) =
            oldItem == newItem
    }
}