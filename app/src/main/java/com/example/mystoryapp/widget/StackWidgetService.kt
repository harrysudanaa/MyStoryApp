package com.example.mystoryapp.widget

import android.content.Intent
import android.widget.RemoteViewsService
import com.example.mystoryapp.di.Injection
import com.example.mystoryapp.view.ViewModelFactory

class StackWidgetService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        val repository = Injection.provideRepository(applicationContext)
        return StackRemoteViewsFactory(applicationContext, repository)
    }
}