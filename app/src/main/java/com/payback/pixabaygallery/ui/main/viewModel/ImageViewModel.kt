package com.payback.pixabaygallery.ui.main.viewModel

import android.annotation.SuppressLint
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.payback.pixabaygallery.api.ApiConstants
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.data.repository.ImagesRepository
import com.payback.pixabaygallery.util.INTERNET_ERROR
import io.reactivex.Scheduler
import javax.inject.Inject

class ImageViewModel
@Inject
constructor(private val imagesRepository: ImagesRepository) : ViewModel() {

    private lateinit var ioScheduler: Scheduler
    private lateinit var mainThread: Scheduler
    private val imagesViewStateMutableLiveData = MutableLiveData<ImagesViewState>()
    val imagesViewStateLiveData: LiveData<ImagesViewState>
        get() = imagesViewStateMutableLiveData

    private val imagesArrayList = ArrayList<Image>()


    /** changing the schedule in testing to be trampoline() scheduler **/
    fun setRxSchedulers(ioScheduler: Scheduler, mainThread: Scheduler) {
        this.ioScheduler = ioScheduler
        this.mainThread = mainThread
    }

    @SuppressLint("CheckResult")
    fun getImages(query: String, page: Int, isSearch: Boolean = false) {
        imagesRepository.getImages(query, page)
            .subscribeOn(ioScheduler)
            .observeOn(mainThread)
            .doOnSubscribe {
                imagesViewStateMutableLiveData.postValue(
                    ImagesViewState(
                        loadingState = View.VISIBLE
                    )
                )
            }
            .subscribe({ imagesList ->
                if (imagesList.isNotEmpty()) {
                    imagesArrayList.addAll(imagesList)
                    imagesViewStateMutableLiveData.postValue(
                        ImagesViewState(
                            imagesDataState = imagesArrayList,
                            imagesListVisibility = View.VISIBLE
                        )
                    )
                } else {
                    if (isSearch) {
                        imagesViewStateMutableLiveData.postValue(
                            ImagesViewState(
                                emptyListErrorVisibility = View.VISIBLE,
                                imagesListVisibility = View.GONE
                            )
                        )
                    }
                }

            },
                {
                    if (it.message == ApiConstants.NO_INTERNET_ERROR) {

                        if (page == 1) {
                            imagesViewStateMutableLiveData.postValue(
                                ImagesViewState(
                                    internetErrorVisibility = View.VISIBLE,
                                    imagesListVisibility = View.GONE
                                )
                            )
                        } else {
                            imagesViewStateMutableLiveData.postValue(
                                ImagesViewState(
                                    errorState = Throwable(INTERNET_ERROR),
                                    imagesListVisibility = if (imagesArrayList.size > 0) View.VISIBLE else View.GONE,
                                    imagesDataState = if (imagesArrayList.size > 0) imagesArrayList else emptyList()
                                )
                            )
                        }

                    } else {
                        imagesViewStateMutableLiveData.postValue(
                            ImagesViewState(
                                errorState = it
                            )
                        )
                    }

                })
    }

    fun clearList() {
        imagesArrayList.clear()
    }
}