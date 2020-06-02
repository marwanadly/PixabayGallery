package com.payback.pixabaygallery.data.remote

import android.annotation.SuppressLint
import com.payback.pixabaygallery.api.ApiConstants
import com.payback.pixabaygallery.api.ImagesApi
import com.payback.pixabaygallery.data.model.Image
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class ImagesRemoteDataSource @Inject constructor(private val imagesApi: ImagesApi) {

    @SuppressLint("CheckResult")
    fun getImages(query: String, page: Int): Single<List<Image>> {
        return imagesApi.getImages(ApiConstants.API_KEY, query = query, page = page)
            .flatMap { response ->
                Observable.fromIterable(response.images.map {
                    it.searchQuery = query
                    it.pageNumber = page
                    return@map it
                }).toList()
            }
    }
}