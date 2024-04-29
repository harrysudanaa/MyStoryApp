package com.example.mystoryapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.mystoryapp.R
import com.example.mystoryapp.data.repository.StoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal class StackRemoteViewsFactory(private val context: Context, private val repository: StoryRepository) : RemoteViewsService.RemoteViewsFactory {

    private val widgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val imageList = repository.getAllStoryImages()
        imageList.forEach { storyItem ->
            val imageUri = Uri.parse(storyItem.imageStory)
            val imageBitmap = uriToBitmap(context, imageUri)
            if (imageBitmap != null) {
                widgetItems.add(imageBitmap)
            }
        }
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, widgetItems[position])

        val extras = bundleOf(
            ListStoryWidget.EXTRA_ITEM to position
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

    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        // implement glide convert to bitmap
        // Use Glide to convert uri to bitmap
        return Glide.with(context)
            .asBitmap()
            .load(uri)
            .submit()
            .get()
    }

}