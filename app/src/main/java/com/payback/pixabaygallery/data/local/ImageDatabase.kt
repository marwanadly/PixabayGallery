package com.payback.pixabaygallery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.payback.pixabaygallery.data.model.Image


@Database(entities = [Image::class], version = 1, exportSchema = false)
abstract class ImageDatabase : RoomDatabase() {
    abstract fun imageDao(): ImageDao
}