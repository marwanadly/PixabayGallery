package com.payback.pixabaygallery.di.module

import com.payback.pixabaygallery.di.scope.FragmentScope
import com.payback.pixabaygallery.ui.main.ImagesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ImageModule {
    @FragmentScope
    @ContributesAndroidInjector
    abstract fun bindImagesFragment(): ImagesFragment
}