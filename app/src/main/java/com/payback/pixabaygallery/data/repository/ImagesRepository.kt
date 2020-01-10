package com.payback.pixabaygallery.data.repository

import com.payback.pixabaygallery.api.ApiConstants
import com.payback.pixabaygallery.data.local.ImagesLocalDataSource
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.data.remote.ImagesRemoteDataSource
import com.payback.pixabaygallery.util.ConnectivityUtility.Companion.isOnline
import io.reactivex.Observable
import io.reactivex.Single
import timber.log.Timber
import javax.inject.Inject

class ImagesRepository @Inject constructor(
    private val imagesRemoteDataSource: ImagesRemoteDataSource,
    private val imagesLocalDataSource: ImagesLocalDataSource
) {

    fun getImages(query: String, page: Int): Single<List<Image>> {
        return imagesLocalDataSource.getImages(query, page)
            .flatMap {
                if (isOnline()) {
                    return@flatMap getRemoteImages(query, page)
                }else {
                    if (it.isNotEmpty()){
                        return@flatMap Observable.fromIterable(it).toList()
                    }else {
                        return@flatMap Observable.fromIterable(it)
                            .toList()
                            .doOnSuccess { throw Throwable(ApiConstants.NO_INTERNET_ERROR) }
                    }
                }

            }
    }

    private fun getRemoteImages(query: String, page: Int): Single<List<Image>> {
        if (page == 1){
            imagesLocalDataSource.deleteImagesByQuery(query)
        }
        return imagesRemoteDataSource.getImages(query, page)
            .doOnSuccess { imagesLocalDataSource.insertImages(it) }
    }

}