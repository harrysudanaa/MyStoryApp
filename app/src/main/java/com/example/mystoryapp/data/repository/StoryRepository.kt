package com.example.mystoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.local.datastore.preferences.UserPreference
import com.example.mystoryapp.data.local.room.Story
import com.example.mystoryapp.data.local.room.StoryDao
import com.example.mystoryapp.data.remote.StoryPagingSource
import com.example.mystoryapp.data.remote.response.AddStoryResponse
import com.example.mystoryapp.data.remote.response.DetailStoryResponse
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegisterResponse
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class StoryRepository @Inject constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDao: StoryDao
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
    suspend fun register(name: String, email: String, password: String): RegisterResponse {
        return apiService.register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return apiService.login(email, password)
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, token)
            }
        ).liveData
    }

    suspend fun getStoriesWithLocation(token: String): StoryResponse {
        return apiService.getStoriesWithLocation(token)
    }
    suspend fun getDetailStory(token: String, id: String): DetailStoryResponse {
        return apiService.getDetailStory(token, id)
    }

    suspend fun addStory(token: String, imagePhoto: MultipartBody.Part, description: RequestBody): AddStoryResponse {
        return apiService.addStory(token, imagePhoto, description)
    }

    suspend fun addStoryToDatabase(image: Story) {
        storyDao.insertStory(image)
    }

    fun getStories() = storyDao.getStories()
}