package com.payback.pixabaygallery.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.payback.pixabaygallery.api.ApiConstants
import com.payback.pixabaygallery.api.ImagesApi
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.data.model.ImagesResponse
import com.payback.pixabaygallery.data.remote.ImagesRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ImagesLocalDataSourceTest {
    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    lateinit var imagesLocalDataSource: ImagesLocalDataSource

    @Mock
    lateinit var imagesDao: ImageDao

    @Mock
    lateinit var imagesList: List<Image>

    lateinit var imagesTestObserver: TestObserver<List<Image>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        imagesLocalDataSource = ImagesLocalDataSource(imagesDao)
    }

    @Test
    fun `Fetch images from api`() {
        whenever(imagesDao.getImages(query =  ApiConstants.DEFAULT_SEARCH_QUERY, page = 1))
            .thenReturn(Single.just(imagesList))
        imagesTestObserver = imagesLocalDataSource.getImages(query = ApiConstants.DEFAULT_SEARCH_QUERY, page = 1).test()
        imagesTestObserver.assertValue(imagesList)
    }

    @Test
    fun `Error images from api`() {
        val errorResponse = Exception()
        whenever(imagesDao.getImages(query =  ApiConstants.DEFAULT_SEARCH_QUERY, page = 1))
            .thenReturn(Single.error(errorResponse))
        imagesTestObserver = imagesLocalDataSource.getImages(query = ApiConstants.DEFAULT_SEARCH_QUERY, page = 1).test()
        imagesTestObserver.assertError(errorResponse)
    }
}