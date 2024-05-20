package com.example.mystoryapp.data.local.room.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mystoryapp.data.local.room.entity.Story

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: Story)

    @Query("SELECT * FROM story_table")
    fun getStories(): PagingSource<Int, Story>

    @Query("SELECT * FROM story_table")
    fun getListStories(): List<Story>

    @Query("DELETE FROM story_table")
    fun deleteAllStories()
}