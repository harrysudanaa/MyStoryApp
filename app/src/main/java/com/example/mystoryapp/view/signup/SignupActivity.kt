package com.example.mystoryapp.view.signup

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivitySignupBinding
import com.example.mystoryapp.view.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySignupBinding
    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
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
        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            signupViewModel.register(name, email, password)

            signupViewModel.message.observe(this) { message ->
                if (message != null) {
                    if (message.error == false) {
                        AlertDialog.Builder(this).apply {
                            setTitle("Register")
                            setMessage(message.message.toString())
                            setPositiveButton("Next") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        AlertDialog.Builder(this).apply {
                            setTitle("Register")
                            setMessage(message.message.toString())
                            setPositiveButton("Next") { _, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    }
                }
            }

            signupViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarSignUp.visibility = View.VISIBLE
            // set alpha to 0.5
            val layout = binding.containerSignUp
            layout.alpha = 0.5f
        } else {
            binding.progressBarSignUp.visibility = View.GONE
        }
    }
}