package com.payback.pixabaygallery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.payback.pixabaygallery.data.model.Image
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(images: List<Image>)

    @Query("DELETE FROM images where searchQuery = :query")
    fun deleteByQuery(query: String)

    @Query("SELECT * FROM images where searchQuery = :query AND pageNumber = :page")
    fun getImages(query: String, page: Int): Single<List<Image>>
}