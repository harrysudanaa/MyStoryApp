package com.example.mystoryapp.di

import android.app.Application
import androidx.room.Room
import com.example.mystoryapp.data.local.room.StoryImageDao
import com.example.mystoryapp.data.local.room.StoryImageDatabase
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
    fun provideDatabase(app: Application): StoryImageDatabase {
        return Room.databaseBuilder(
            app,
            StoryImageDatabase::class.java,
            "story_image"
        ).build()
    }

    @Provides
    @Singleton
    fun provideStoryImageDao(storyImageDatabase: StoryImageDatabase): StoryImageDao {
        return storyImageDatabase.storyImageDao()
    }
}