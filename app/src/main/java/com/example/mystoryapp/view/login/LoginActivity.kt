package com.example.mystoryapp.view.login

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.databinding.ActivityLoginBinding
import com.example.mystoryapp.view.ViewModelFactory
import com.example.mystoryapp.view.main.MainActivity

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            loginViewModel.login(email, password)

            loginViewModel.loginStatus.observe(this) { status ->
                if (status.error == false) {
                    AlertDialog.Builder(this).apply {
                        setTitle("Login")
                        setMessage("Login Success!")
                        setPositiveButton("Next") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                    loginViewModel.saveSession(UserModel(email, "${status.loginResult?.token}"))
                } else {
                    AlertDialog.Builder(this).apply {
                        setTitle("Login")
                        setMessage("${status.message}!")
                        setPositiveButton("Next") { _, _ ->
                            finish()
                        }
                        create()
                        show()
                    }
                }
            }

            loginViewModel.isLoading.observe(this) {
                showLoading(it)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBarLogin.visibility = View.VISIBLE
            // set alpha to 0.5
            val layout = binding.containerLogin
            layout.alpha = 0.5f
        } else {
            binding.progressBarLogin.visibility = View.GONE
        }
    }
}