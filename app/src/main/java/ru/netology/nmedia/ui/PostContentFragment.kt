package ru.netology.nmedia.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ru.netology.nmedia.databinding.PostContentFragmentBinding

class PostContentFragment : Fragment() {

    private val args by navArgs<PostContentFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = PostContentFragmentBinding.inflate(
        layoutInflater, container, false
    ).apply {
        editContent.setText(args.initialContent)
        editUrl.setText(args.initialUrl)
        editContent.requestFocus()
        ok.setOnClickListener {
            onOkButtonClicked(this)
        }
    }.root

    private fun onOkButtonClicked(binding: PostContentFragmentBinding) {
        val text = binding.editContent.text
        val url = binding.editUrl.text
        if (!text.isNullOrBlank() &&
            (url.contains("https://www.youtube.com/watch") || url.isNullOrBlank())
        ) {
            val resultBundle = Bundle(2)
            resultBundle.putString(CONTENT_KEY, text.toString())
            resultBundle.putString(URL_KEY, url.toString())
            setFragmentResult(REQUEST_KEY, resultBundle)
        }
        findNavController().popBackStack()
    }

    companion object {
        const val REQUEST_KEY = "requestKey"
        const val CONTENT_KEY = "postNewContent"
        const val URL_KEY = "urlPost"
    }
}