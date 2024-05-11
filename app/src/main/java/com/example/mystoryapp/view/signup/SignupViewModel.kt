package com.example.mystoryapp.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.remote.response.ErrorResponse
import com.example.mystoryapp.data.repository.StoryRepository
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _message = MutableLiveData<ErrorResponse>()
    val message : LiveData<ErrorResponse> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.register(name, email, password)
                val jsonResponse = Gson().toJson(response)
                _message.value = Gson().fromJson(jsonResponse, ErrorResponse::class.java)
            } catch (e: HttpException) {
                _isLoading.value = true
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
                _message.value = errorBody
            } finally {
                _isLoading.value = false
            }
        }
    }
}