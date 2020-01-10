package com.payback.pixabaygallery.data.model

import com.google.gson.annotations.SerializedName
import com.payback.pixabaygallery.data.model.Image

data class ImagesResponse (
    @SerializedName("hits")
    var images: List<Image>
)