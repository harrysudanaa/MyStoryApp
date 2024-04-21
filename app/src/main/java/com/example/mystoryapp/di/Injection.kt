package com.example.mystoryapp.di

import android.content.Context
import com.example.mystoryapp.data.UserRepository
import com.example.mystoryapp.data.local.datastore.preferences.UserPreference
import com.example.mystoryapp.data.local.datastore.preferences.dataStore

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        return UserRepository.getInstance(pref)
    }
}