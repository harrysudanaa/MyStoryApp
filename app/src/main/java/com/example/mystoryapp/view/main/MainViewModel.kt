package com.example.mystoryapp.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.local.room.StoryImage
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story: LiveData<List<ListStoryItem>> = _story

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStories(token: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getStories(token).listStory
                _story.value = response
            } catch (e: HttpException) {
                _isLoading.value = true
                e.response()?.errorBody()?.let { Log.d("MainViewModel", it.string()) }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun addImageToDatabase(image: StoryImage) {
        viewModelScope.launch {
            repository.addImageToDatabase(image)
        }
    }

}