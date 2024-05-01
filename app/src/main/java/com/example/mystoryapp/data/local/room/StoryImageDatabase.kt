package com.example.mystoryapp.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [StoryImage::class], version = 1, exportSchema = false)
abstract class StoryImageDatabase: RoomDatabase() {
    abstract fun storyImageDao(): StoryImageDao

}