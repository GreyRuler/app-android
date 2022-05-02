package ru.netology.nmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.Post
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.PostBinding

internal class PostsAdapter(
    private val interactionListener: PostInteractionListener
) : ListAdapter<Post, PostsAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = PostBinding.inflate(inflater, parent, false)
        return ViewHolder(binding, interactionListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: PostBinding,
        listener: PostInteractionListener
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var post: Post

        private val popupMenu by lazy {
            PopupMenu(itemView.context, binding.options).apply {
                inflate(R.menu.options_post)
                setOnMenuItemClickListener { menuItem ->
                    when(menuItem.itemId) {
                        R.id.remove -> {
                            listener.onRemoveClicked(post)
                            true
                        }
                        R.id.edit -> {
                            listener.onEditClicked(post)
                            true
                        }
                        else -> false
                    }

                }
            }
        }

        init {
            with(binding) {
                like.setOnClickListener {
                     listener.onLikeClicked(post)
                }
                share.setOnClickListener {
                     listener.onShareClicked(post)
                }
            }
        }

        fun bind(post: Post) {
            this.post = post
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countLike.text = conversionCountLike(post.countLike)
                like.setImageResource(getLikeIconResID(post.likeByMe))
                countShare.text = post.countShare.toString()
                options.setOnClickListener { popupMenu.show() }
            }
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