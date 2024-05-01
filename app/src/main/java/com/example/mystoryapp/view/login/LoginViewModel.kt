package com.example.mystoryapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.repository.StoryRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<LoginResponse>()
    val loginStatus: LiveData<LoginResponse> = _loginStatus

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = repository.login(email, password)
                val jsonResponse = Gson().toJson(response)
                _loginStatus.value = Gson().fromJson(jsonResponse, LoginResponse::class.java)
            } catch (e: HttpException) {
                _isLoading.value = true
                val errorBodyString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(errorBodyString, LoginResponse::class.java)
                _loginStatus.value = errorBody
            } finally {
                _isLoading.value = false
            }
        }
    }
}