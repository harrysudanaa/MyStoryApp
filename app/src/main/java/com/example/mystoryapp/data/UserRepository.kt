package com.example.mystoryapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.local.datastore.preferences.UserPreference
import com.example.mystoryapp.data.remote.response.LoginResponse
import com.example.mystoryapp.data.remote.response.RegisterResponse
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.data.remote.retrofit.ApiService
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository private constructor(private val userPreference: UserPreference) {

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
        return ApiConfig.getApiService().register(name, email, password)
    }

    suspend fun login(email: String, password: String): LoginResponse {
        return ApiConfig.getApiService().login(email, password)
    }

    companion object {
        const val TAG = "UserRepository"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference)
            }.also { instance = it }
    }
}