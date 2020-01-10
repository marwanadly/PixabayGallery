package com.payback.pixabaygallery.ui.main

import com.payback.pixabaygallery.data.model.Image

interface OnImageClick {
    fun onClick(image: Image)
}