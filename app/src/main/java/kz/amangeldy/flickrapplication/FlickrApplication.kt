package kz.amangeldy.flickrapplication

import android.app.Application
import kz.amangeldy.flickrapplication.di.mainModule
import org.koin.android.ext.android.startKoin

class FlickrApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(mainModule))
    }
}