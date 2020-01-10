package com.payback.pixabaygallery

import com.payback.pixabaygallery.di.component.DaggerApplicationComponent
import com.squareup.leakcanary.LeakCanary
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber


class PixabayGalleryApplication : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())

        if (LeakCanary.isInAnalyzerProcess(this)) return

        LeakCanary.install(this)

        val builder = Picasso.Builder(this)
        builder.downloader(OkHttp3Downloader(this, Long.MAX_VALUE))
        Picasso.setSingletonInstance(builder.build())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.builder().application(this).build()
    }


}