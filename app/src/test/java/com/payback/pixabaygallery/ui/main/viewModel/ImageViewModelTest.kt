package com.payback.pixabaygallery.ui.main.viewModel

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.whenever
import com.payback.pixabaygallery.api.ApiConstants
import com.payback.pixabaygallery.api.ApiConstants.Companion.DEFAULT_SEARCH_QUERY
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.data.repository.ImagesRepository
import com.payback.pixabaygallery.util.INTERNET_ERROR
import com.payback.pixabaygallery.util.UNEXPECTED_ERROR
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ImageViewModelTest {

    @Rule
    @JvmField
    val ruleForLivaData = InstantTaskExecutorRule()

    lateinit var imageViewModel: ImageViewModel

    @Mock
    lateinit var imagesRepository: ImagesRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        imageViewModel = ImageViewModel(imagesRepository)
        imageViewModel.setRxSchedulers(Schedulers.trampoline(), Schedulers.trampoline())
    }

    @Test
    fun `Fetch images with default search query`() {
        whenever(imagesRepository.getImages(DEFAULT_SEARCH_QUERY, 1)).thenReturn(
            Single.just(
                getMockedImageList()
            )
        )
        imageViewModel.getImages(DEFAULT_SEARCH_QUERY, 1)
        Assert.assertEquals(
            imageViewModel.imagesViewStateLiveData.value,
            ImagesViewState(imagesDataState = getMockedImageList())
        )
    }

    @Test
    fun `Error in fetching images due to internet problem when recyclerview is not empty`() {
        val errorResponse = Throwable(ApiConstants.NO_INTERNET_ERROR)
        whenever(imagesRepository.getImages(DEFAULT_SEARCH_QUERY, 2)).thenReturn(
            Single.error(
                errorResponse
            )
        )
        imageViewModel.getImages(DEFAULT_SEARCH_QUERY, 2)
        Assert.assertEquals(
            imageViewModel.imagesViewStateLiveData.value?.errorState?.message,
            INTERNET_ERROR
        )
    }

    @Test
    fun `Error in fetching images due to internet problem when recyclerview is empty`() {
        val errorResponse = Throwable(ApiConstants.NO_INTERNET_ERROR)
        whenever(imagesRepository.getImages(DEFAULT_SEARCH_QUERY, 1)).thenReturn(
            Single.error(
                errorResponse
            )
        )
        imageViewModel.getImages(DEFAULT_SEARCH_QUERY, 1)
        Assert.assertEquals(
            imageViewModel.imagesViewStateLiveData.value, ImagesViewState(
                internetErrorVisibility = View.VISIBLE,
                imagesListVisibility = View.GONE
            )
        )
    }

    @Test
    fun `Show empty search result`() {
        whenever(imagesRepository.getImages(DEFAULT_SEARCH_QUERY, 1)).thenReturn(
            Single.just(
                emptyList()
            )
        )
        imageViewModel.getImages(DEFAULT_SEARCH_QUERY, 1, true)
        Assert.assertEquals(
            imageViewModel.imagesViewStateLiveData.value,
            ImagesViewState(
                emptyListErrorVisibility = View.VISIBLE,
                imagesListVisibility = View.GONE
            )
        )
    }

    @Test
    fun `Error fetching images due to unexpected error`() {
        val errorResponse = Throwable(UNEXPECTED_ERROR)
        whenever(imagesRepository.getImages(DEFAULT_SEARCH_QUERY, 1)).thenReturn(
            Single.error(
                errorResponse
            )
        )
        imageViewModel.getImages(DEFAULT_SEARCH_QUERY, 1)
        Assert.assertEquals(
            imageViewModel.imagesViewStateLiveData.value, ImagesViewState(
                errorState = errorResponse
            )
        )
    }

    private fun getMockedImageList(): ArrayList<Image> {
        val x = ArrayList<Image>()
        x.add(Image(0, 0, "", "", "", "", "", 0, 0, 0, "", 0))
        x.add(Image(0, 0, "", "", "", "", "", 0, 0, 0, "", 0))
        x.add(Image(0, 0, "", "", "", "", "", 0, 0, 0, "", 0))
        x.add(Image(0, 0, "", "", "", "", "", 0, 0, 0, "", 0))
        return x
    }
}