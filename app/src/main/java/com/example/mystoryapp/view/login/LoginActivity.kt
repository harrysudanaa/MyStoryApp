package com.example.mystoryapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.view.main.MainActivity
import com.example.mystoryapp.view.signup.SignupActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupAnimation()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun setupAnimation() {
        val duration: Long = 400

        with(binding) {
            val ivLoginAnimate =
                ObjectAnimator.ofFloat(ivLogin, View.ALPHA, 1f).setDuration(duration)
            val tvLoginTitleAnimate =
                ObjectAnimator.ofFloat(tvLoginTitle, View.ALPHA, 1f).setDuration(duration)
            val edEmailLayoutAnimate =
                ObjectAnimator.ofFloat(edEmailLayout, View.ALPHA, 1f).setDuration(duration)
            val edLoginEmailAnimate =
                ObjectAnimator.ofFloat(edLoginEmail, View.ALPHA, 1f).setDuration(duration)
            val edPasswordLayoutAnimate =
                ObjectAnimator.ofFloat(edPasswordLayout, View.ALPHA, 1f).setDuration(duration)
            val edLoginPasswordAnimate =
                ObjectAnimator.ofFloat(edLoginPassword, View.ALPHA, 1f).setDuration(duration)
            val btnLoginAnimate =
                ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(duration)
            val tvMoveToSignupAnimate =
                ObjectAnimator.ofFloat(tvMoveToSignup, View.ALPHA, 1f).setDuration(duration)
            val tvSignupAnimate =
                ObjectAnimator.ofFloat(tvSignup, View.ALPHA, 1f).setDuration(duration)

            AnimatorSet().apply {
                playSequentially(
                    ivLoginAnimate,
                    tvLoginTitleAnimate,
                    edEmailLayoutAnimate,
                    edLoginEmailAnimate,
                    edPasswordLayoutAnimate,
                    edLoginPasswordAnimate,
                    btnLoginAnimate,
                    tvMoveToSignupAnimate,
                    tvSignupAnimate
                )
                start()
            }
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

        with(binding) {
            ivLogin.alpha = 0f
            tvLoginTitle.alpha = 0f
            edLoginEmail.alpha = 0f
            edLoginPassword.alpha = 0f
            btnLogin.alpha = 0f
            tvMoveToSignup.alpha = 0f
            tvSignup.alpha = 0f
            edEmailLayout.alpha = 0f
            edPasswordLayout.alpha = 0f
        }
    }

    private fun setupAction() {
        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()

            with(loginViewModel) {
                login(email, password)

                loginStatus.observe(this@LoginActivity) { status ->
                    if (status.error == false) {
                        loginViewModel.saveSession(
                            UserModel(
                                email = email,
                                token = "${status.loginResult?.token}"
                            )
                        )
                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle(getString(R.string.login_title_alert))
                            setMessage(getString(R.string.login_message_alert))
                            setPositiveButton(getString(R.string.next_action)) { _, _ ->
                                moveToMainActivity()
                            }
                            create()
                            show()
                        }
                    } else {
                        AlertDialog.Builder(this@LoginActivity).apply {
                            setTitle(getString(R.string.login_title_alert))
                            setMessage("${status.message}!")
                            setPositiveButton(getString(R.string.next_action)) { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }

                isLoading.observe(this@LoginActivity) {
                    showLoading(it)
                }
            }
        }
    }

    private fun moveToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        val layout = binding.containerLogin
        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
            layout.alpha = 0.5f
        } else {
            binding.progressBarLogin.visibility = View.GONE
        }
    }
}