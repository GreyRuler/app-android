package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.util.hideKeyBoard
import ru.netology.nmedia.util.showKeyBoard
import ru.netology.nmedia.viewModel.PostViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<PostViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }
        binding.saveButton.setOnClickListener {
            with(binding.contentEditText) {
                val content = text.toString()
                viewModel.onSaveButtonClicked(content)
                binding.groupEditMode.visibility = View.GONE
                clearFocus()
                hideKeyBoard()
            }
        }
        binding.closeEditModeButton.setOnClickListener {
            with(binding) {
                textEditMessage.text = ""
                contentEditText.setText("")
                groupEditMode.visibility = View.GONE
            }
        }
        viewModel.currentPost.observe(this) { currentPost ->
            val content = currentPost?.content
            if (content != null) {
                binding.contentEditText.showKeyBoard()
                binding.groupEditMode.visibility = View.VISIBLE
            }
            with(binding.contentEditText) {
                setText(content)
                binding.textEditMessage.text = content
            }
        }
    }
}