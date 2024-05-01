package com.example.mystoryapp.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViewsService
import com.example.mystoryapp.data.repository.StoryRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StackWidgetService : RemoteViewsService() {

    @Inject
    lateinit var repository: StoryRepository

    @Inject
    lateinit var context: Context
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(context, repository)
    }
}