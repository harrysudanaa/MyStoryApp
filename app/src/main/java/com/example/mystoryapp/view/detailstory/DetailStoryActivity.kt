package com.example.mystoryapp.view.detailstory

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityDetailStoryBinding
import com.example.mystoryapp.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val detailStoryViewModel by viewModels<DetailStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
    }

    private fun setupView() {
        val idStory = intent.getStringExtra(EXTRA_ID)
        idStory?.let { detailStoryViewModel.getDetailStory(idStory) }

        detailStoryViewModel.detailStory.observe(this) { detailStory ->
            binding.tvDetailStoryTitle.text = detailStory.story?.name
            binding.tvDetailStoryDesc.text = detailStory.story?.description

            Glide.with(this)
                .load(detailStory.story?.photoUrl)
                .into(binding.ivDetailStory)
        }

        detailStoryViewModel.isLoading.observe(this) {
            showLoading(it)
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