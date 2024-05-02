package com.example.mystoryapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(image: Story)

    @Query("SELECT * FROM story_table ORDER BY createdAt DESC LIMIT 10")
    fun getStories() : List<Story>
}