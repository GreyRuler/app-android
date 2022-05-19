package ru.netology.nmedia.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.Post
import ru.netology.nmedia.adapter.PostInteractionListener
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.repo.PostRepository
import ru.netology.nmedia.repo.impl.SQLiteRepository
import ru.netology.nmedia.util.SingleLiveEvent

class FeedFragmentViewModel(
    application: Application
) : AndroidViewModel(application), PostInteractionListener {

    private val repository: PostRepository =
        SQLiteRepository(
            dao = AppDb.getInstance(
                context = application
            ).postDao
        )

    val data by repository::data

    val sharePostContent = SingleLiveEvent<String>()
    val navigateToPostContentScreen = SingleLiveEvent<Post>()
    val navigateToPostScreen = SingleLiveEvent<Post>()
    val uri = SingleLiveEvent<String>()
    private val currentPost = MutableLiveData<Post?>(null)

    fun onSaveButtonClicked(content: String, url: String?) {
        if (content.isEmpty()) return
        val post = currentPost.value?.copy(
            content = content,
            url = url
        ) ?: Post(
            id = PostRepository.NEW_POST_ID,
            author = "Me",
            content = content,
            published = "Today",
            likes = 0,
            reposts = 0,
            url = url
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
        navigateToPostContentScreen.value = post
    }

    override fun onLikeClicked(post: Post) =
        repository.like(post.id)

    override fun onShareClicked(post: Post) {
        sharePostContent.value = post.content
    }

    override fun onPostClicked(post: Post) {
        navigateToPostScreen.value = post
    }

    // endregion PostInteractionListener
}