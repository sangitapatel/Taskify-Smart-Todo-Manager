package com.taskify.todomanager

import android.app.Application
import com.taskify.todomanager.di.dataModule
import com.taskify.todomanager.di.appModule
import com.taskify.todomanager.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApplication)
            modules(
                appModule, dataModule, uiModule
            )
        }
    }
}