package com.example.mystoryapp

import com.example.mystoryapp.data.local.room.entity.Story

object DataDummy {
    fun generateDummyStoryResponse(): List<Story> {
        val items: MutableList<Story> = arrayListOf()
        for (i in 0..100) {
            val quote = Story(
                i.toString(),
                "user + $i",
                "description $i",
                "photoUrl $i",
                "createdAt $i"
            )
            items.add(quote)
        }
        return items
    }
}