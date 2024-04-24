package com.example.mystoryapp.view.main

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.view.StoryAdapter
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.addstory.AddStoryActivity
import com.example.mystoryapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val storyAdapter = StoryAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.getSession().observe(this) { user ->
            if (!user.isLogin) {
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
            }
        }

        setupView()
        setupAction()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.logout -> {
                AlertDialog.Builder(this).apply {
                    setTitle("Logout")
                    setMessage("Are you sure to logout?")
                    setPositiveButton("Yes") { _, _ ->
                        mainViewModel.logout()
                    }
                    setNegativeButton("No") { _, _ ->
                        return@setNegativeButton
                    }
                    create()
                    show()
                }
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
        setSupportActionBar(binding.toolbarMain.root)

        binding.rvListStory.layoutManager = LinearLayoutManager(this)
        binding.rvListStory.adapter = storyAdapter

        mainViewModel.getStories()

        mainViewModel.story.observe(this) {story ->
            setStoryData(story)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
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