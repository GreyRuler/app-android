package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.clearFragmentResultListener
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.FeedFragmentBinding
import ru.netology.nmedia.viewModel.FeedFragmentViewModel

class FeedFragment : Fragment() {

    private val viewModel by viewModels<FeedFragmentViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.sharePostContent.observe(this) { postContent ->
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, postContent)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(
                intent, getString(R.string.chooser_share_post)
            )
            startActivity(shareIntent)
        }

        setFragmentResultListener(
            requestKey = PostContentFragment.REQUEST_KEY_FEED_FRAGMENT
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY_FEED_FRAGMENT) return@setFragmentResultListener
            val postContent = bundle.getString(
                PostContentFragment.CONTENT_KEY
            ) ?: return@setFragmentResultListener
            val postUrl = bundle.getString(
                PostContentFragment.URL_KEY
            )
            viewModel.onSaveButtonClicked(postContent, postUrl)
        }

        viewModel.navigateToPostContentScreen.observe(this) { post ->
            val direction = FeedFragmentDirections
                .toPostContentFragment(post?.content, post?.url, "requestKeyFeedFragment")
            findNavController().navigate(direction)
        }

        viewModel.navigateToPostScreen.observe(this) { post ->
            val direction = FeedFragmentDirections.toPostFragment(post)
            findNavController().navigate(direction)
        }

        viewModel.uri.observe(this) { url ->
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse(url)
            )
            startActivity(intent)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )= FeedFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        val adapter = PostsAdapter(viewModel)
        binding.postsRecyclerView.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            adapter.submitList(posts)
        }
        binding.fab.setOnClickListener {
            viewModel.onAddClicked()
        }
    }.root
}
