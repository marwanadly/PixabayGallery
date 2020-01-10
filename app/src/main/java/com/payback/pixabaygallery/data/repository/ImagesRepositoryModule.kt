package com.payback.pixabaygallery.data.repository

import com.payback.pixabaygallery.api.ImagesApi
import com.payback.pixabaygallery.data.local.ImageDao
import com.payback.pixabaygallery.data.local.ImagesLocalDataModule
import com.payback.pixabaygallery.data.local.ImagesLocalDataSource
import com.payback.pixabaygallery.data.remote.ImagesRemoteDataModule
import com.payback.pixabaygallery.data.remote.ImagesRemoteDataSource
import com.payback.pixabaygallery.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module(includes = [ImagesRemoteDataModule::class, ImagesLocalDataModule::class])
class ImagesRepositoryModule {

    @Provides
    @ApplicationScope
    fun provideImageRemoteDataSource(imagesApi: ImagesApi): ImagesRemoteDataSource {
        return ImagesRemoteDataSource(imagesApi = imagesApi)
    }

    @Provides
    @ApplicationScope
    fun provideImageLocalDataSource(imageDao: ImageDao): ImagesLocalDataSource {
        return ImagesLocalDataSource(imageDao = imageDao)
    }
}