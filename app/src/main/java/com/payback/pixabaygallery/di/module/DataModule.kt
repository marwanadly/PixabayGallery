package com.payback.pixabaygallery.di.module

import com.payback.pixabaygallery.data.local.ImagesLocalDataSource
import com.payback.pixabaygallery.data.remote.ImagesRemoteDataSource
import com.payback.pixabaygallery.data.repository.ImagesRepository
import com.payback.pixabaygallery.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    @ApplicationScope
    fun providesImageRepository(imagesRemoteDataSource: ImagesRemoteDataSource,
                                imagesLocalDataSource: ImagesLocalDataSource
    ): ImagesRepository {
        return ImagesRepository(imagesRemoteDataSource, imagesLocalDataSource)
    }
}