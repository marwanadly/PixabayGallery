package com.payback.pixabaygallery.ui.main.viewModel

import android.view.View
import com.payback.pixabaygallery.data.model.Image

data class ImagesViewState(
    var imagesDataState: List<Image> = emptyList(),
    var errorState: Throwable? = null,
    var loadingState: Int = View.GONE,
    var internetErrorVisibility: Int = View.GONE,
    var imagesListVisibility: Int = View.VISIBLE,
    var emptyListErrorVisibility: Int = View.GONE
)