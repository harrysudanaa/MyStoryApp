package com.example.mystoryapp.view.addstory

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.remote.response.AddStoryResponse
import com.example.mystoryapp.data.repository.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _status = MutableLiveData<AddStoryResponse>()
    val status: LiveData<AddStoryResponse> = _status

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun addStory(imagePhoto: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.addStory(imagePhoto, description)
                _status.value = response
                Log.d("AddStoryViewModel", "tes success")
            } catch (e: HttpException) {
                _isLoading.value = true
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
                Log.e("AddStoryViewModel", errorResponse.message.toString())
            } finally {
                _isLoading.value = false
            }
        }
    }

}