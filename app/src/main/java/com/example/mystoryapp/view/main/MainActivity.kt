package com.example.mystoryapp.view.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.menu.MenuBuilder
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.room.Story
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.view.StoryAdapter
import com.example.mystoryapp.view.addstory.AddStoryActivity
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.maps.MapsActivity
import com.example.mystoryapp.view.settings.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel>()
    private val storyAdapter = StoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                setContentView(binding.root)
                setupView()
                setupAction()
            }
        }
    }

    @SuppressLint("RestrictedApi")
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        if (menu is MenuBuilder) {
            menu.setOptionalIconsVisible(true)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle(getString(R.string.logout))
                    setMessage(getString(R.string.logout_confirmation))
                    setPositiveButton(getString(R.string.positive_button)) { _, _ ->
                        mainViewModel.logout()
                    }
                    setNegativeButton(getString(R.string.negative_button)) { _, _ ->
                        return@setNegativeButton
                    }
                    create()
                    show()
                }
                true
            }
            R.id.settings -> {
                val intent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.maps -> {
                val intent = Intent(this@MainActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupAction() {
        binding.fabAddStory.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }

        with (binding) {
            setSupportActionBar(toolbarMain.root)
            rvListStory.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = storyAdapter
            }
        }

        with (mainViewModel) {
            getSession().observe(this@MainActivity) { user ->
                getStories("Bearer ${user.token}")
            }

            story.observe(this@MainActivity) { story ->
                if (story.isEmpty()) {
                    binding.tvEmptyData.visibility = View.VISIBLE
                }
                setStoryData(story)
                story.forEach { storyItem ->
                    with (storyItem) {
                        val storyData = Story(
                            id = id.toString(),
                            name = name.toString(),
                            description = description.toString(),
                            photoUrl = photoUrl.toString(),
                            createdAt = createdAt.toString()
                        )
                        addStoryToDatabase(storyData)
                    }
                }
            }

            isLoading.observe(this@MainActivity) {
                showLoading(it)
            }
        }
    }

    private fun setStoryData(story: List<ListStoryItem>) {
        storyAdapter.submitList(story)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarMain.visibility = View.VISIBLE
        } else {
            binding.progressBarMain.visibility = View.GONE

        }
    }
}