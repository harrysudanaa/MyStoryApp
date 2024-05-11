package com.example.mystoryapp.view.detailstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.remote.response.DetailStoryResponse
import com.example.mystoryapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class DetailStoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _detailStory = MutableLiveData<DetailStoryResponse>()
    val detailStory: LiveData<DetailStoryResponse> = _detailStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getDetailStory(token: String, id: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.getDetailStory(token, id)
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