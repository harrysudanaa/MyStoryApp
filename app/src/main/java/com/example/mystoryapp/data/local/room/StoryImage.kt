package com.example.mystoryapp.data.local.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "story_image_table")
data class StoryImage (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val imageStory: String? = null
)