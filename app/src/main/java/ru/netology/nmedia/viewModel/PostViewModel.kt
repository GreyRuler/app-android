package ru.netology.nmedia.viewModel

import androidx.lifecycle.ViewModel
import ru.netology.nmedia.repo.PostRepository
import ru.netology.nmedia.repo.impl.InMemoryPostRepository

class PostViewModel : ViewModel() {

    private val repository: PostRepository = InMemoryPostRepository()

    val data by repository::data

    fun onLikedClicked() = repository.like()

    fun onShareClicked() = repository.share()
}