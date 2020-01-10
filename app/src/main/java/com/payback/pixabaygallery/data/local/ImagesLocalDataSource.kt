package com.payback.pixabaygallery.data.local

import android.annotation.SuppressLint
import com.payback.pixabaygallery.data.model.Image
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ImagesLocalDataSource @Inject constructor(private val imageDao: ImageDao) {

    fun getImages(query: String, page: Int) : Single<List<Image>> {
        return imageDao.getImages(query = query, page = page)
    }

    @SuppressLint("CheckResult")
    fun deleteImagesByQuery(query: String) {
        Completable.fromRunnable { imageDao.deleteByQuery(query = query) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    @SuppressLint("CheckResult")
    fun insertImages(images: List<Image>) {
        Completable.fromRunnable { imageDao.insertAll(images) }
            .subscribeOn(Schedulers.io())
            .subscribe()
    }
}