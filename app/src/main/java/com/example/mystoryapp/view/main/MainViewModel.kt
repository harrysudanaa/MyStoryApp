package com.example.mystoryapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.local.room.entity.Story
import com.example.mystoryapp.data.remote.response.ListStoryItem
import com.example.mystoryapp.data.repository.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StoryRepository
) : ViewModel() {

    private val _userSession = MutableLiveData<UserModel>()
    val userSession: LiveData<UserModel> = _userSession

    private val _stories = MutableLiveData<PagingData<ListStoryItem>>()
    val stories: LiveData<PagingData<ListStoryItem>> = _stories

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getSession()
    }

    fun getSession() {
        viewModelScope.launch {
            repository.getSession().collectLatest { user ->
                _userSession.value = user
                getStories("Bearer ${user.token}")
            }
        }
    }

    fun getStories(token: String): LiveData<PagingData<Story>> {
        return repository.getStories(token).cachedIn(viewModelScope)
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

//    fun addStoryToDatabase(story: Story) {
//        viewModelScope.launch {
//            repository.addStoryToDatabase(story)
//        }
//    }

}