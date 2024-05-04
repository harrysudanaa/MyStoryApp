package com.example.mystoryapp.view.detailstory

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val detailStoryViewModel by viewModels<DetailStoryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val idStory = intent.getStringExtra(EXTRA_ID)

        with (detailStoryViewModel) {
            getSession().observe(this@DetailStoryActivity) { user ->
                val userToken = "Bearer ${user.token}"
                idStory?.let { getDetailStory(userToken, idStory) }
            }

            detailStory.observe(this@DetailStoryActivity) { detailStory ->
                binding.tvDetailName.text = detailStory.story?.name
                binding.tvDetailDescription.text = detailStory.story?.description

                Glide.with(this@DetailStoryActivity)
                    .load(detailStory.story?.photoUrl)
                    .into(binding.ivDetailPhoto)
            }

            isLoading.observe(this@DetailStoryActivity) {
                showLoading(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarDetailStory.visibility = View.VISIBLE
        } else {
            binding.progressBarDetailStory.visibility = View.GONE
        }
    }

    companion object {
        const val EXTRA_ID = "id"
    }
}