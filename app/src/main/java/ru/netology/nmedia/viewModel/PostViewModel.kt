package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.repo.PostRepository
import ru.netology.nmedia.repo.impl.FilePostRepository
import ru.netology.nmedia.repo.impl.InMemoryPostRepository
import ru.netology.nmedia.repo.impl.SharedPrefsPostRepository
import ru.netology.nmedia.util.SingleLiveEvent

class PostViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository =
        FilePostRepository(application)

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreen = SingleLiveEvent<Unit>()
    val editPost = SingleLiveEvent<Post>()
    val uri = SingleLiveEvent<String>()
    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: List<String?>) {
        if (content.isNullOrEmpty()) return
        val post = currentPost.value?.copy(
            content = content[0]!!,
            url = content[1]
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content[0]!!,
            published = "Today",
            countLike = 0,
            countShare = 0,
            url = content[1]
        )
        repository.save(post)
        currentPost.value = null
    }

    fun onAddClicked() {
        navigateToPostContentScreen.call()
    }

    // region PostInteractionListener

    override fun onPlayClicked(post: Post) {
        uri.value = post.url!!
    }

    override fun onRemoveClicked(post: Post) =
        repository.delete(post.id)

    override fun onEditClicked(post: Post) {
        currentPost.value = post
        editPost.value = post
    }

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
    }

    // endregion PostInteractionListener
}