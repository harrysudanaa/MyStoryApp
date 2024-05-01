package com.example.mystoryapp.view.welcome

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.databinding.ActivityWelcomeBinding
import com.example.mystoryapp.view.login.LoginActivity
import com.example.mystoryapp.view.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupAnimation()
    }

    private fun setupAnimation() {
        val duration: Long = 500

        val imageViewAnimate = ObjectAnimator.ofFloat(binding.ivWelcome, View.ALPHA, 1f).setDuration(duration)
        val loginButtonAnimate = ObjectAnimator.ofFloat(binding.btnLoginWelcome, View.ALPHA, 1f).setDuration(duration)
        val signupButtonAnimate = ObjectAnimator.ofFloat(binding.btnSignupWelcome, View.ALPHA, 1f).setDuration(duration)
        val titleAnimate = ObjectAnimator.ofFloat(binding.tvWelcomeTitle, View.ALPHA, 1f).setDuration(duration)
        val descAnimate = ObjectAnimator.ofFloat(binding.tvWelcomeDesc, View.ALPHA, 1f).setDuration(duration)

        val together = AnimatorSet().apply {
            playTogether(loginButtonAnimate, signupButtonAnimate)
        }

        AnimatorSet().apply {
            playSequentially(imageViewAnimate, titleAnimate, descAnimate, together)
            start()
        }
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.btnLoginWelcome.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnSignupWelcome.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}