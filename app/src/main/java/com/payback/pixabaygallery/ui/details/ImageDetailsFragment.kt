package com.payback.pixabaygallery.ui.details


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.payback.pixabaygallery.R
import com.payback.pixabaygallery.databinding.FragmentImageDetailsBinding


class ImageDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentImageDetailsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_image_details, container, false)
        binding.image = arguments?.getParcelable("image")
        return binding.root
    }


}
