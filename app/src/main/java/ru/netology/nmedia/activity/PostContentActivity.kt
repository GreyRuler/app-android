package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.PostContentActivityBinding

class PostContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = PostContentActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val editContent = intent.getStringExtra("content")
        val editUrl = intent.getStringExtra("url")
        binding.editContent.setText(editContent)
        binding.editUrl.setText(editUrl)
        binding.editContent.requestFocus()
        binding.ok.setOnClickListener {
            val intent = Intent()
            val text = binding.editContent.text
            val url = binding.editUrl.text
            if (text.isNullOrBlank() ||
                (!url.contains("https://www.youtube.com/watch") && !url.isNullOrBlank())) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = text.toString()
                val urlPost = url.toString()
                intent.putExtra(RESULT_KEY, content)
                intent.putExtra(URL_KEY, urlPost)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    object ResultContract : ActivityResultContract<List<String?>?, List<String?>?>() {

        override fun createIntent(context: Context, input: List<String?>?) =
            Intent(context, PostContentActivity::class.java).apply {
                putExtra("content", input?.get(0))
                putExtra("url", input?.get(1))
            }

        override fun parseResult(resultCode: Int, intent: Intent?) =
            if (resultCode == Activity.RESULT_OK) {
                listOf(
                    intent?.getStringExtra(RESULT_KEY),
                    intent?.getStringExtra(URL_KEY)
                )
            } else null
    }

    private companion object {
        private const val RESULT_KEY = "postNewContent"
        private const val URL_KEY = "urlPost"
    }
}