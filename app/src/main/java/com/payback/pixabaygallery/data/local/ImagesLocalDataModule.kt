package com.payback.pixabaygallery.data.local

import android.content.Context
import androidx.room.Room
import com.payback.pixabaygallery.di.scope.ApplicationScope
import dagger.Module
import dagger.Provides


@Module
class ImagesLocalDataModule {

    @ApplicationScope
    @Provides
    fun provideImagesDatabase(context: Context): ImageDatabase {
        return Room.databaseBuilder(
            context,
            ImageDatabase::class.java,
            "image_database"
        ).build()
    }

    @ApplicationScope
    @Provides
    fun provideImageDao(imageDatabase: ImageDatabase) : ImageDao {
        return imageDatabase.imageDao()
    }

}