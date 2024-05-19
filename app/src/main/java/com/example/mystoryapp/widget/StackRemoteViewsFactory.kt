package com.example.mystoryapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.room.entity.Story
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.utils.uriToBitmap

class StackRemoteViewsFactory(private val context: Context, private val repository: StoryRepository) : RemoteViewsService.RemoteViewsFactory {

    private val widgetItems = ArrayList<Bitmap>()
    private val listStories = ArrayList<Story>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        widgetItems.clear()
        listStories.clear()
        try {
            val stories = repository.getListStories()
            stories.forEach { storyItem ->
                val imageUri = Uri.parse(storyItem.photoUrl)
                val imageBitmap = uriToBitmap(context, imageUri)
                if (imageBitmap != null) {
                    widgetItems.add(imageBitmap)
                }
                listStories.add(storyItem)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, widgetItems[position])

        val extras = bundleOf(
            ListStoryWidget.EXTRA_ITEM to listStories[position].id
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}