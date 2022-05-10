package ru.netology.nmedia.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.R
import ru.netology.nmedia.adapter.PostsAdapter
import ru.netology.nmedia.databinding.PostFragmentBinding
import ru.netology.nmedia.viewModel.PostFragmentViewModel

class PostFragment : Fragment() {

    private val args by navArgs<PostFragmentArgs>()
    private val viewModel by viewModels<PostFragmentViewModel>()

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
            requestKey = PostContentFragment.REQUEST_KEY
        ) { requestKey, bundle ->
            if (requestKey != PostContentFragment.REQUEST_KEY) return@setFragmentResultListener
            val postContent = bundle.getString(
                PostContentFragment.CONTENT_KEY
            ) ?: return@setFragmentResultListener
            val postUrl = bundle.getString(
                PostContentFragment.URL_KEY
            )
            viewModel.onSaveButtonClicked(postContent, postUrl)
        }

        viewModel.navigateToPostContentScreen.observe(this) { post ->
            val direction = PostFragmentDirections.actionPostFragmentToPostContentFragment(post?.content, post?.url)
            findNavController().navigate(direction)
        }

        viewModel.navigateToPostScreen.observe(this) {
            findNavController().popBackStack()
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
    ) = PostFragmentBinding.inflate(
        layoutInflater, container, false
    ).also { binding ->
        val holder = PostsAdapter.ViewHolder(binding, viewModel)
        viewModel.data.observe(viewLifecycleOwner) {
            holder.bind(args.post)
        }
    }.root
}