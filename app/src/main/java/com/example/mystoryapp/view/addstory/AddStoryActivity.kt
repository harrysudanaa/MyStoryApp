package com.example.mystoryapp.view.addstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityAddStoryBinding
import com.example.mystoryapp.utils.reduceFileImage
import com.example.mystoryapp.utils.uriToFile
import com.example.mystoryapp.view.camera.CameraActivity
import com.example.mystoryapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private val addStoryViewModel by viewModels<AddStoryViewModel>()

    private var currentImageUri: Uri? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var latitude: String? = null
    private var longitude: String? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            showToast(getString(R.string.permission_request_granted))
        } else {
            showToast(getString(R.string.permission_request_denied))
        }
    }

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                showToast("Fine location access granted")
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                showToast("Only approximate location access granted")
            } else -> showToast("Location access denied")
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            showToast(getString(R.string.no_media_selected))
        }
    }

    private val launcherCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CameraActivity.CAMERA_RESULT) {
            currentImageUri = it.data?.getStringExtra(CameraActivity.EXTRA_IMAGE)?.toUri()
            showImage()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.cbLocation.setOnCheckedChangeListener { _, isChecked ->
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            if (isChecked) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    getLocationAccess()
                }
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        latitude = location.latitude.toString()
                        longitude = location.longitude.toString()
                    }
                }
            }
        }

        binding.btnGallery.setOnClickListener {
            startGallery()
        }

        binding.btnCamera.setOnClickListener {
            startCameraX()
        }

        binding.buttonAdd.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                addStoryViewModel.isLoading.observe(this) {
                    showLoading(it)
                }
                addStory()
            }
            addStoryViewModel.status.observe(this) { status ->
                if (status.error == false) {
                    showToast(status.message.toString())
                    val intent = Intent(this, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                } else {
                    showToast("${status.message}")
                }
            }
        }
    }

    private fun getLocationAccess() {
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION)
        )
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        currentImageUri?.let { binding.ivAddStory.setImageURI(it) }
    }

    private fun allPermissionsGranted() = ContextCompat.checkSelfPermission(
        this,
        REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherCameraX.launch(intent)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun addStory() {
        addStoryViewModel.getSession().observe(this) { user ->
            val userToken = "Bearer ${user.token}"
            currentImageUri?.let { uri ->
                val imageFile = uriToFile(uri, this).reduceFileImage()
                val description = binding.edAddDescription.text.toString()

                if (imageFile.length() > 1024 * 1024) {
                    // When image size greater than 1 MB
                    showToast(getString(R.string.image_size_limit))
                } else {
                    val requestDescription = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val requestLatitude = latitude?.toRequestBody("text/plain".toMediaType())
                    val requestLongitude = longitude?.toRequestBody("text/plain".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile
                    )

                    addStoryViewModel.addStory(userToken, multipartBody, requestDescription, requestLatitude, requestLongitude)
                }
            } ?: showToast(getString(R.string.empty_image))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        val layout = binding.addStoryContainer
        if (isLoading) {
            binding.progressBarAddStory.visibility = View.VISIBLE
            layout.alpha = 0.5f
        } else {
            binding.progressBarAddStory.visibility = View.GONE
            layout.alpha = 1f
        }
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}