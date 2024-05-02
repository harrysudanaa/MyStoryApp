package com.example.mystoryapp.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.example.mystoryapp.R
import com.example.mystoryapp.data.local.datastore.preferences.UserModel
import com.example.mystoryapp.data.repository.StoryRepository
import com.example.mystoryapp.utils.uriToBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class StackRemoteViewsFactory(private val context: Context, private val repository: StoryRepository) : RemoteViewsService.RemoteViewsFactory {

    private val widgetItems = ArrayList<Bitmap>()

    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        widgetItems.clear()
        try {
            val imageList = repository.getStories()
            println("factory widget: $imageList")
            imageList.forEach { storyItem ->
                val imageUri = Uri.parse(storyItem.photoUrl)
                val imageBitmap = uriToBitmap(context, imageUri)
                if (imageBitmap != null) {
                    widgetItems.add(imageBitmap)
                }
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

}