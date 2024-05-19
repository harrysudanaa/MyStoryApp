package com.example.mystoryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.mystoryapp.data.local.room.dao.RemoteKeysDao
import com.example.mystoryapp.data.local.room.dao.StoryDao
import com.example.mystoryapp.data.local.room.entity.RemoteKeys
import com.example.mystoryapp.data.local.room.entity.Story

@Database(entities = [Story::class, RemoteKeys::class], version = 2, exportSchema = false)
abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao
}