package com.melardev.android.crud

import android.app.Application

class CrudApplication : Application() {

    companion object {
        lateinit var instance: CrudApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}