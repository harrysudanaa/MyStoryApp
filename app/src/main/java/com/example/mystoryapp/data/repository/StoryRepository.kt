package com.example.mystoryapp.data.repository

import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.local.datastore.preferences.UserPreference
import com.example.mystoryapp.data.remote.response.AddStoryResponse
import com.example.mystoryapp.data.remote.response.DetailStoryResponse
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegisterResponse
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference
    ) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    // TODO: register user using retrofit
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    suspend fun getStories(): StoryResponse {
        return apiService.getStories()
    }

    suspend fun getDetailStory(id: String): DetailStoryResponse {
        return apiService.getDetailStory(id)
    }

    suspend fun addStory(imagePhoto: MultipartBody.Part, description: RequestBody): AddStoryResponse {
        return apiService.addStory(imagePhoto, description)
    }

    companion object {
        const val TAG = "UserRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, userPreference)
            }.also { instance = it }
    }
}