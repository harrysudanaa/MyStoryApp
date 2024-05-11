package com.example.mystoryapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.remote.response.AddStoryResponse
import com.example.mystoryapp.data.repository.StoryRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _status = MutableLiveData<AddStoryResponse>()
    val status: LiveData<AddStoryResponse> = _status

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun addStory(token: String, imagePhoto: MultipartBody.Part, description: RequestBody) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.addStory(token, imagePhoto, description)
                _status.value = response
            } catch (e: HttpException) {
                _isLoading.value = true
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddStoryResponse::class.java)
                _status.value = errorResponse
            } finally {
                _isLoading.value = false
            }
        }
    }

}