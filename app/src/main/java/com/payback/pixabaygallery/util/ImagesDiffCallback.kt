package com.payback.pixabaygallery.util

import androidx.recyclerview.widget.DiffUtil
import com.payback.pixabaygallery.data.model.Image

class ImagesDiffCallback(
    private val oldImagesList: List<Image>,
    private val newImagesList: List<Image>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldImagesList[oldItemPosition] == newImagesList[newItemPosition]

    override fun getOldListSize(): Int = oldImagesList.size

    override fun getNewListSize(): Int = newImagesList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldImagesList[oldItemPosition] == newImagesList[newItemPosition]

}