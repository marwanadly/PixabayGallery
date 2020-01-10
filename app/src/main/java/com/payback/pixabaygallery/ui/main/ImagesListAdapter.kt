package com.payback.pixabaygallery.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.payback.pixabaygallery.R
import com.payback.pixabaygallery.data.model.Image
import com.payback.pixabaygallery.databinding.ImageItemBinding
import com.payback.pixabaygallery.util.ImagesDiffCallback

class ImagesListAdapter(private val onImageClick: OnImageClick) :
    RecyclerView.Adapter<ImagesListAdapter.ImageViewHolder>() {

    private val imagesList = ArrayList<Image>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.image_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imagesList[position])
    }

    override fun getItemCount(): Int = imagesList.size

    fun submitList(list: List<Image>) {
        val diffCallback = ImagesDiffCallback(this.imagesList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.imagesList.clear()
        this.imagesList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }


    inner class ImageViewHolder(private val imageItemBinding: ImageItemBinding) :
        RecyclerView.ViewHolder(imageItemBinding.root) {
        fun bind(image: Image) {
            imageItemBinding.image = image
            imageItemBinding.imageContainer.setOnClickListener { onImageClick.onClick(image = image) }
        }
    }


}