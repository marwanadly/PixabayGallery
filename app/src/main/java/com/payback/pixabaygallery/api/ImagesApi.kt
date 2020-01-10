package com.payback.pixabaygallery.api

import com.payback.pixabaygallery.data.model.ImagesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {
    @GET("api")
    fun getImages(@Query("key") apiKey: String, @Query("q") query: String, @Query("page") page: Int): Single<ImagesResponse>
}