package com.example.mystoryapp.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import java.io.File
import java.util.Date
import java.util.Locale

fun createCustomTempFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val filesDir = context.externalCacheDir
    return File.createTempFile(timeStamp, ".jpg", filesDir)
}