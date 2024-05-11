package com.example.mystoryapp.view.maps

import android.util.Log
import retrofit2.HttpException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.mystoryapp.data.remote.response.StoryResponse
import com.example.mystoryapp.data.repository.StoryRepository
import androidx.lifecycle.viewModelScope
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.remote.response.ListStoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(
    private val repository: StoryRepository
): ViewModel() {

    private val _location = MutableLiveData<List<ListStoryItem>>()
    val location: LiveData<List<ListStoryItem>> = _location

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun getStoriesWithLocation(token: String) {
        viewModelScope.launch {
            try {
                val response = repository.getStoriesWithLocation(token).listStory
                _location.value = response
            } catch (e: HttpException) {
                e.response()?.errorBody()?.let { Log.d("MapsViewModel", it.string()) }
            }
        }
    }
}