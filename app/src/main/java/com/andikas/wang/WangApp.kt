package com.andikas.wang

import android.app.Application
import com.andikas.wang.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class WangApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            if (BuildConfig.DEBUG) {
                androidLogger(Level.DEBUG)
            }
            androidContext(this@WangApp)
            modules(appModule)
        }
    }
}
