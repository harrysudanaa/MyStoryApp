package com.example.mystoryapp.view.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mystoryapp.databinding.ActivitySettingsBinding
import com.example.mystoryapp.view.main.MainActivity
import java.util.Locale

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    @Suppress("DEPRECATION")
    private fun setupAction() {
        binding.btnInLocale.setOnClickListener {
            val languageCode = "in"
            val config = resources.configuration
            config.locale = Locale(languageCode)
            resources.updateConfiguration(config, resources.displayMetrics)

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.btnEnLocale.setOnClickListener {
            val languageCode = "en"
            val config = resources.configuration
            config.locale = Locale(languageCode)
            resources.updateConfiguration(config, resources.displayMetrics)

            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }
}