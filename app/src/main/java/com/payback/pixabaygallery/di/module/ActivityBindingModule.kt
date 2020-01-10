package com.payback.pixabaygallery.di.module

import com.payback.pixabaygallery.di.scope.ActivityScope
import com.payback.pixabaygallery.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBindingModule {
    @ActivityScope
    @ContributesAndroidInjector(modules = [ImageModule::class])
    abstract fun mainActivity(): MainActivity
}