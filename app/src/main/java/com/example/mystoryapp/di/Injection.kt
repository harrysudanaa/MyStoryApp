package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.local.datastore.preferences.UserPreference
import com.example.mystoryapp.data.local.datastore.preferences.dataStore
import com.example.mystoryapp.data.local.room.StoryImageDatabase
import com.example.mystoryapp.data.remote.retrofit.ApiConfig
import com.example.mystoryapp.data.repository.StoryRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking{ pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        val storyImageDao = StoryImageDatabase.getDatabaseInstance(context.applicationContext).storyImageDao()
        return StoryRepository.getInstance(apiService, pref, storyImageDao)
    }
}