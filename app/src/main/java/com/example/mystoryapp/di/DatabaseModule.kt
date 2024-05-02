package com.example.mystoryapp.di

import android.app.Application
import androidx.room.Room
import com.example.mystoryapp.data.local.room.StoryDao
import com.example.mystoryapp.data.local.room.StoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(app: Application): StoryDatabase {
        return Room.databaseBuilder(
            app,
            StoryDatabase::class.java,
            "story"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStoryDao(storyDatabase: StoryDatabase): StoryDao {
        return storyDatabase.storyDao()
    }
}