package com.payback.pixabaygallery.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.payback.pixabaygallery.api.ApiConstants.Companion.DEFAULT_SEARCH_QUERY
import com.payback.pixabaygallery.data.local.ImagesLocalDataSource
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.data.remote.ImagesRemoteDataSource
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ImagesRepositoryTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    lateinit var imagesRepository: ImagesRepository

    @Mock
    lateinit var imagesRemoteDataSource: ImagesRemoteDataSource

    @Mock
    lateinit var imagesLocalDataSource: ImagesLocalDataSource

    lateinit var imagesTestObserver: TestObserver<List<Image>>

    @Mock
    lateinit var imagesList: List<Image>

    var isOnline = false

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        imagesRepository = ImagesRepository(imagesRemoteDataSource, imagesLocalDataSource)
    }

    @Test
    fun `Fetch images from api incase there is internet`() {
        isOnline = true
        whenever(
            imagesLocalDataSource.getImages(
                query = DEFAULT_SEARCH_QUERY,
                page = 1
            )
        ).thenReturn(Single.just(emptyList()))
        whenever(
            imagesRemoteDataSource.getImages(
                query = DEFAULT_SEARCH_QUERY,
                page = 1
            )
        ).thenReturn(
            Single.just(imagesList)
        )

        imagesTestObserver = imagesRepository.getImages(query = DEFAULT_SEARCH_QUERY, page = 1)
            .flatMap {
                if (isOnline){
                    return@flatMap imagesRemoteDataSource.getImages(query = DEFAULT_SEARCH_QUERY, page = 1)
                }
                return@flatMap imagesLocalDataSource.getImages(query = DEFAULT_SEARCH_QUERY, page = 1)
            }
            .test()
        imagesTestObserver.assertValue(imagesList)
    }

    @Test
    fun `Fetch images from local database incase there is no internet`() {
        isOnline = false
        whenever(
            imagesLocalDataSource.getImages(
                query = DEFAULT_SEARCH_QUERY,
                page = 1
            )
        ).thenReturn(Single.just(imagesList))

        whenever(
            imagesRemoteDataSource.getImages(
                query = DEFAULT_SEARCH_QUERY,
                page = 1
            )
        ).thenReturn(
            Single.just(emptyList())
        )

        imagesTestObserver = imagesRepository.getImages(query = DEFAULT_SEARCH_QUERY, page = 1)
            .flatMap {
                if (isOnline){
                    return@flatMap imagesRemoteDataSource.getImages(query = DEFAULT_SEARCH_QUERY, page = 1)
                }
                return@flatMap imagesLocalDataSource.getImages(query = DEFAULT_SEARCH_QUERY, page = 1)

            }
            .test()
        imagesTestObserver.assertValue(imagesList)

    }
}