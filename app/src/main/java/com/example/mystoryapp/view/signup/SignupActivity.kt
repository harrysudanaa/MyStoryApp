package com.example.mystoryapp.view.signup

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
import com.example.mystoryapp.databinding.ActivitySignupBinding
import com.example.mystoryapp.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
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
        with (binding) {
            signupButton.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                with (signupViewModel) {
                    register(name, email, password)

                    message.observe(this@SignupActivity) { message ->
                        if (message != null) {
                            if (message.error == false) {
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle(getString(R.string.register_title_alert))
                                    setMessage(message.message.toString())
                                    setPositiveButton(getString(R.string.next_action)) { _, _ ->
                                        moveToLoginActivity()
                                    }
                                    create()
                                    show()
                                }
                            } else {
                                AlertDialog.Builder(this@SignupActivity).apply {
                                    setTitle(getString(R.string.register_title_alert))
                                    setMessage(message.message.toString())
                                    setPositiveButton(getString(R.string.next_action)) { _, _ ->
                                        finish()
                                        startActivity(intent)
                                    }
                                    create()
                                    show()
                                }
                            }
                        }
                    }

                    isLoading.observe(this@SignupActivity) {
                        showLoading(it)
                    }
                }
            }
        }
    }

    private fun moveToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarSignUp.visibility = View.VISIBLE
            val layout = binding.containerSignUp
            layout.alpha = 0.5f
        } else {
            binding.progressBarSignUp.visibility = View.GONE
        }
    }
}