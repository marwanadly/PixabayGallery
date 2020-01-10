package com.payback.pixabaygallery.di.component

import android.app.Application
import com.payback.pixabaygallery.PixabayGalleryApplication
import com.payback.pixabaygallery.data.repository.ImagesRepositoryModule
import com.payback.pixabaygallery.di.module.*
import com.payback.pixabaygallery.di.scope.ApplicationScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@ApplicationScope
@Component(
    modules = [ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ViewModelModule::class,
        ImagesRepositoryModule::class,
        DataModule::class,
        ActivityBindingModule::class]
)
interface ApplicationComponent : AndroidInjector<PixabayGalleryApplication> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}