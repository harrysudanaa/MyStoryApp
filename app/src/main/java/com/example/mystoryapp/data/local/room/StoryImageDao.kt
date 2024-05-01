package com.example.mystoryapp.data.local.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface StoryImageDao {
    @Insert
    suspend fun insertImage(image: StoryImage)

    @Query("SELECT * FROM story_image_table")
    fun getAllStoryImage() : List<StoryImage>
}