package com.example.mystoryapp.view.detailstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.remote.response.DetailStoryResponse
import com.example.mystoryapp.data.repository.StoryRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class DetailStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory: LiveData<DetailStoryResponse> = _detailStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getDetailStory(id)
                _detailStory.value = response

            } catch (e: HttpException) {
                _isLoading.value = true
                e.response()?.errorBody()?.let { Log.d("DetailStoryViewModel", it.string()) }
            } finally {
                _isLoading.value = false
            }
        }
    }
}