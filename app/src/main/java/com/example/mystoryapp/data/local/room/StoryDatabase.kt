package com.example.mystoryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Story::class], version = 1, exportSchema = false)
abstract class StoryDatabase: RoomDatabase() {
    abstract fun storyDao(): StoryDao

}