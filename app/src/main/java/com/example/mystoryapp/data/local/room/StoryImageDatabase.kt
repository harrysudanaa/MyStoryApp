package com.example.mystoryapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StoryImage::class], version = 1, exportSchema = false)
abstract class StoryImageDatabase: RoomDatabase() {
    abstract fun storyImageDao(): StoryImageDao

    companion object {
        private fun buildDatabase(context: Context): StoryImageDatabase {
            return Room.databaseBuilder(
                context,
                StoryImageDatabase::class.java,
                "story_image"
            ).build()
        }

        @Volatile
        private var INSTANCE: StoryImageDatabase? = null

        fun getDatabaseInstance(context: Context): StoryImageDatabase {
            if (INSTANCE == null) {
                synchronized(this) {
                    INSTANCE = buildDatabase(context)
                }
            }
            return INSTANCE!!
        }
    }
}