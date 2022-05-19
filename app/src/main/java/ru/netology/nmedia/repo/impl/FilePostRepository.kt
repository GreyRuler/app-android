package ru.netology.nmedia.repo.impl

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.Post
import ru.netology.nmedia.repo.PostRepository
import kotlin.properties.Delegates

class FilePostRepository(
    private val application: Application
) : PostRepository {

    private val gson = Gson()
    private val type = TypeToken.getParameterized(
        List::class.java, Post::class.java
    ).type

    private val prefs = application.getSharedPreferences(
        "repo", Context.MODE_PRIVATE
    )

    private var nextId: Long by Delegates.observable(
        prefs.getLong(NEXT_ID_PREFS_KEY, 0L)
    ) { _, _, newValue ->
        prefs.edit {
            putLong(NEXT_ID_PREFS_KEY, newValue)
        }
    }

    private var posts
        get() = checkNotNull(data.value) {
            "Data value should not be null"
        }
        set(value) {
            application.openFileOutput(
                FILE_NAME,
                Context.MODE_PRIVATE
            ).bufferedWriter().use {
                it.write(gson.toJson(value))
            }
            data.value = value
        }

    override val data: MutableLiveData<List<Post>>

    init {
        val postsFile = application.filesDir.resolve(FILE_NAME)
        val posts: List<Post> = if (postsFile.exists()) {
            val inputStream = application.openFileInput(FILE_NAME)
            val reader = inputStream.bufferedReader()
            reader.use { gson.fromJson(reader, type) }
        } else emptyList()
        data = MutableLiveData(posts)
    }

    override fun like(postID: Long) {
        posts = posts.map {
            if (it.id != postID) it
            else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likes = it.likes + if (it.likedByMe) -1 else +1
                )
            }
        }
    }

    override fun share(postID: Long) {
        posts = posts.map {
            if (it.id != postID) it
            else it.copy(reposts = it.reposts + 1)
        }
    }

    override fun delete(postID: Long) {
        posts = posts.filterNot { it.id == postID }
    }

    override fun save(post: Post) {
        if (post.id == PostRepository.NEW_POST_ID) insert(post) else update(post)
    }

    private fun insert(post: Post) {
        posts = listOf(
            post.copy(id = ++nextId)
        ) + posts
    }

    private fun update(post: Post) {
        posts = posts.map {
            if (post.id == it.id) post else it
        }
    }

    companion object {
        private var instance: FilePostRepository? = null
        fun getInstance(app: Application) = instance ?: FilePostRepository(app).also { instance = it }
        const val NEXT_ID_PREFS_KEY = "nextID"
        const val FILE_NAME = "posts.json"
    }

//    companion object : SingletonRepositoryHolder<FilePostRepository, Application>(::FilePostRepository) {
//        const val NEXT_ID_PREFS_KEY = "nextID"
//        const val FILE_NAME = "posts.json"
//    }
}