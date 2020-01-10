package com.payback.pixabaygallery.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.payback.pixabaygallery.di.ViewModelKey
import com.payback.pixabaygallery.di.scope.ApplicationScope
import com.payback.pixabaygallery.ui.main.viewModel.ImageViewModel
import com.payback.pixabaygallery.ui.main.viewModel.ImageViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ImageViewModel::class)
    abstract fun bindImagesViewModel(imageViewMode: ImageViewModel): ViewModel

    @Binds
    @ApplicationScope
    abstract fun bindViewModelFactory(imageViewModelFactory: ImageViewModelFactory): ViewModelProvider.Factory
}