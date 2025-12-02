package com.example.myapplication.shared.utils

import android.util.Log

class AppLogger {
    companion object{
        private val DEFAULT_TAG = "AppLogger"
    }

    fun info(tag: String = DEFAULT_TAG, message: String) {
        Log.i(tag, message)
    }

    fun debug(tag: String = DEFAULT_TAG, message: String) {
        Log.d(tag, message)
    }

    fun warning(tag: String = DEFAULT_TAG, message: String) {
        Log.w(tag, message)
    }
}