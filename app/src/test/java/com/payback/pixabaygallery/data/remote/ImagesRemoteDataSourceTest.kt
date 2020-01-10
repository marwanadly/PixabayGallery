package com.payback.pixabaygallery.data.remote

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.payback.pixabaygallery.api.ApiConstants
import com.payback.pixabaygallery.api.ApiConstants.Companion.DEFAULT_SEARCH_QUERY
import com.payback.pixabaygallery.api.ImagesApi
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.data.model.ImagesResponse
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ImagesRemoteDataSourceTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    lateinit var imagesRemoteDataSource: ImagesRemoteDataSource

    @Mock
    lateinit var imagesApi: ImagesApi

    @Mock
    lateinit var imagesResponse: ImagesResponse

    lateinit var imagesTestObserver: TestObserver<List<Image>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        imagesRemoteDataSource = ImagesRemoteDataSource(imagesApi)
    }

    @Test
    fun `Fetch images from api`() {
        whenever(imagesApi.getImages(ApiConstants.API_KEY, query =  DEFAULT_SEARCH_QUERY, page = 1))
            .thenReturn(Single.just(imagesResponse))
        imagesTestObserver = imagesRemoteDataSource.getImages(query = DEFAULT_SEARCH_QUERY, page = 1).test()
        imagesTestObserver.assertValue(imagesResponse.images)
    }

    @Test
    fun `Error images from api`() {
        val errorResponse = Exception()
        whenever(imagesApi.getImages(ApiConstants.API_KEY, query =  DEFAULT_SEARCH_QUERY, page = 1))
            .thenReturn(Single.error(errorResponse))
        imagesTestObserver = imagesRemoteDataSource.getImages(query = DEFAULT_SEARCH_QUERY, page = 1).test()
        imagesTestObserver.assertError(errorResponse)
    }
}